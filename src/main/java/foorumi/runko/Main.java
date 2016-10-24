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
            map.put("alueet", alueDao.etsiKaikki()); 
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
        
        get("/alue/:alue_id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aiheet", aiheDao.etsiKaikki());
            map.put("alue", alueDao.etsiYksi(Integer.parseInt(req.params(":alue_id"))));
            return new ModelAndView(map, "aiheet");
        }, new ThymeleafTemplateEngine());

        post("/alue/:alue_id", (req, res) -> {
            String aihe = req.queryParams("aihe");
            aihe = aihe.trim();
            if (aihe.length() > 0 || aihe.length() < 40) {
                aiheDao.tallenna(aihe, req.queryParams("alue_id"));
                return "ok";
            }
            res.redirect("/alue");
            return "ok";
        });
        
        get("/alue/:alue_id/aihe/:aihe_id", (req, res) -> { 
            HashMap map = new HashMap<>();
            map.put("viestit", viestiDao.etsiKymmenenUusinta());
            map.put("aihe", aiheDao.etsiYksi(Integer.parseInt(req.params(":aihe_id"))));
            map.put("alue", alueDao.etsiYksi(Integer.parseInt(req.params(":alue_id"))));
            
            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());
        
        post("/alue/:alueid/aihe/:aihe_id", (req, res) -> {
            String lahettaja = req.queryParams("lähettäjä");
            String teksti = req.queryParams("teksti");
            if (lahettaja.trim().length() > 0 && teksti.trim().length() > 0) {
                viestiDao.tallenna(req.queryParams("teksti"), req.queryParams("lahettaja"),req.params(":aihe_id"));
            }
            res.redirect("/alue/" + req.params(":alue_id") + "/aihe/" + Integer.parseInt(req.params(":aihe_id")));
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
