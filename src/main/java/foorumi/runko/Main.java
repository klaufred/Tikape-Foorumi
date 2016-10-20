package foorumi.runko;

import foorumi.database.AiheDao;
import foorumi.database.AlueDao;
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import foorumi.database.Database;
import foorumi.database.ViestiDao;
import spark.Spark;

public class Main {

    public static void main(String[] args) throws Exception {
        port(getHerokuAssignedPort());

        Database database = new Database("foorumi.db");
        // Database ... 
        
        AlueDao alueDao = new AlueDao(database);
        AiheDao aiheDao = new AiheDao(database);
        ViestiDao viestiDao = new ViestiDao(database);
        
        Spark.get("/", (req, res) -> {
            res.redirect("/aihe");
            return "ok";
        });
        
        Spark.get("/tehtavat/poista/:id", (req, res) -> {
            int aihe = aiheDao.getId(":id");
            aiheDao.poista(req.params(":id"));
            res.redirect("/aihe/" + req.params());
            return "ok";
        });

        Spark.get("/tehtavat", (req, res) -> {
            HashMap data = new HashMap<>();
            data.put("tehtavat", todoDao.haeTodot());

            return new ModelAndView(data, "index");
        }, new ThymeleafTemplateEngine());

        Spark.post("/tehtavat", (req, res) -> {
            todoDao.lisaa(req.queryParams("tehtava"));
            res.redirect("/");
            return "ok";
        });
        
        Spark.get("/tekijat", (req, res) -> {
            HashMap data = new HashMap<>();
            data.put("tekijat", tekijaDao.haeTekijat());

            return new ModelAndView(data, "tekijat");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/aihe/:id", (req, res) -> {
            HashMap data = new HashMap<>();
            data.put("tehtavat", todoDao.haeTodot(Integer.parseInt(req.params(":id"))));

            return new ModelAndView(data, "index");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/tekijat", (req, res) -> {
            tekijaDao.luoTekija(req.queryParams("nimi"));
            res.redirect("/");
            return "ok";
        });

        Spark.post("/tekijat/:id", (req, res) -> {
            todoDao.lisaa(req.params(":id"), 
                    req.queryParams("tehtava"));

            res.redirect("/tekijat/" + req.params(":id"));
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
