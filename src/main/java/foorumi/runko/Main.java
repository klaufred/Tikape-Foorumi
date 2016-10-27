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
            map.put("alueet", alueDao.haeAiheetJaViestit());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        post("/", (req, res) -> {
            String alue = req.queryParams("alue");
            alue = alue.trim();
            if (alue.length() > 0 && alue.length() < 40) {
                alueDao.tallenna(alue);
            }
            res.redirect("/");
            return "ok";
        });
        
        get("/alue/:alueId", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aiheet", aiheDao.haeAiheet(Integer.parseInt(req.params(":alueId"))));
            map.put("alue", alueDao.etsiYksi(Integer.parseInt(req.params(":alueId"))));
            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

        post("/alue/:alueId", (req, res) -> {
            String aihe = req.queryParams("aihe");
            if (aihe.length() > 0 && aihe.length() < 40) {
                aiheDao.tallenna(aihe, Integer.parseInt(req.params(":alueId")));
            }
            res.redirect("/alue/" + req.params(":alueId"));
            return "ok";
        });
        
        get("/aihe/:aiheId", (req, res) -> { 
            HashMap map = new HashMap<>();
            map.put("viestit", viestiDao.etsiKymmenenUusinta(Integer.parseInt(req.params(":aiheId"))));
            map.put("aihe", aiheDao.etsiYksi(Integer.parseInt(req.params(":aiheId"))));
            return new ModelAndView(map, "viestiketju");
        }, new ThymeleafTemplateEngine());
        
        post("/aihe/:aiheId", (req, res) -> {
            String lahettaja = req.queryParams("lähettäjä");
            String teksti = req.queryParams("teksti");
            if (lahettaja.trim().length() > 0 && teksti.trim().length() > 0) {
                viestiDao.tallenna(req.queryParams("teksti"), req.queryParams("lähettäjä"),req.params(":aiheId"));
            }
            res.redirect("/aihe/" + Integer.parseInt(req.params(":aiheId")));
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
