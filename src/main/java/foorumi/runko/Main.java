package foorumi.runko;

import foorumi.database.AiheDao;
import foorumi.database.AlueDao;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import foorumi.database.Database;
import foorumi.database.ViestiDao;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) throws Exception {
        port(getHerokuAssignedPort());

        Database database = new Database("jdbc:sqlite:foorumi.db");
        
        AlueDao alueDao = new AlueDao(database);
        AiheDao aiheDao = new AiheDao(database);
        ViestiDao viestiDao = new ViestiDao(database);
        
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.etsiKaikki(1)); // 1 vain koska Dao vaatii että sillä on integer 
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        post("/", (req, res) -> {
            String alueNimi = req.queryParams("alue");
            alueNimi = alueNimi.trim();
            if (alueNimi.length() > 0 && alueNimi.length() < 40) {
                alueDao.tallenna(alueNimi);
            }
            res.redirect("/");
            return "ok";
        });
        
        get("/alue/:alueId", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aiheet", aiheDao.etsiKaikki(Integer.parseInt(req.params(":alueId"))));
            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

        post("/alue/:alueId", (req, res) -> {
            String aihe = req.queryParams("aihe");
            if (aihe.length() > 0 && aihe.length() < 40) {
                aiheDao.tallenna(aihe, req.queryParams(":alueId"));
            }
            res.redirect("/alue/" + req.params(":alueId"));
            return "ok";
        });
        
        get("/alue/:alueId/aihe/:aiheId", (req, res) -> { 
            HashMap map = new HashMap<>();
            map.put("viestit", viestiDao.etsiKymmenenUusinta(Integer.parseInt(req.params(":aiheId"))));
            
            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());
        
        post("/alue/:alueid/aihe/:aiheid", (req, res) -> {
            String lahettaja = req.queryParams("lähettäjä");
            String teksti = req.queryParams("teksti");
            if (lahettaja.trim().length() > 0 && teksti.trim().length() > 0) {
                viestiDao.tallenna(req.queryParams("teksti"), req.queryParams("lahettaja"),req.params(":aihe"));
            }
            res.redirect("/alue/" + req.params(":alue") + "/aihe/" + Integer.parseInt(req.params(":aihe")));
            return "ok";
        });
    }
    
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
}
}
