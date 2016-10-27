package foorumi.database;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }

        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        // heroku käyttää SERIAL-avainsanaa uuden tunnuksen automaattiseen luomiseen
        lista.add("CREATE TABLE Alue (alue_id SERIAL PRIMARY KEY, nimi varchar(100) NOT NULL);");
        lista.add("CREATE TABLE Aihe (aihe_id SERIAL PRIMARY KEY, alue integer NOT NULL, teksti varchar(100) NOT NULL, FOREIGN KEY(alue) REFERENCES Alue(alue_id));");
        lista.add("CREATE TABLE Viesti (viesti_id SERIAL PRIMARY KEY, aihe integer NOT NULL, teksti varchar(400) NOT NULL, lähettäjä varchar(50) NOT NULL, aika String, FOREIGN KEY(aihe) REFERENCES Aihe(aihe_id));");

        return lista;
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add("CREATE TABLE Alue (alue_id integer PRIMARY KEY, nimi varchar(100) NOT NULL);");
        lista.add("CREATE TABLE Aihe (aihe_id integer PRIMARY KEY, alue integer NOT NULL, teksti varchar(100) NOT NULL, FOREIGN KEY(alue) REFERENCES Alue(alue_id));");
        lista.add("CREATE TABLE Viesti (viesti_id integer PRIMARY KEY, aihe integer NOT NULL, teksti varchar(400) NOT NULL, lähettäjä varchar(50) NOT NULL, aika String, FOREIGN KEY(aihe) REFERENCES Aihe(aihe_id));");
        
        lista.add("INSERT INTO Alue (nimi) VALUES ('Kiinnostava alue')");
        lista.add("INSERT INTO Alue (nimi) VALUES ('Tylsä alue')");
        
        lista.add("INSERT INTO Aihe (teksti, alue) VALUES ('Tikape', 1)");
        lista.add("INSERT INTO Aihe (teksti, alue) VALUES ('Ohpe', 1)");
        lista.add("INSERT INTO Aihe (teksti, alue) VALUES ('edistystä', 2)");
        lista.add("INSERT INTO Aihe (teksti, alue) VALUES ('maailma', 2)");
        
        lista.add("INSERT INTO Viesti (aihe, teksti, lähettäjä, aika) VALUES (1, 'Onnistuuko?', 'Klaus', Datetime('now)");
        lista.add("INSERT INTO Viesti (aihe, teksti, lähettäjä, aika) VALUES (2, 'Onnistuuko? Vai ei?', 'Klaus', Datetime('now)");
        lista.add("INSERT INTO Viesti (aihe, teksti, lähettäjä, aika) VALUES (3, 'Saankohan toimimaan?', 'Klaus', Datetime('now)");
        lista.add("INSERT INTO Viesti (aihe, teksti, lähettäjä, aika) VALUES (3, 'täällä taas?', 'K', Datetime('now)");
        lista.add("INSERT INTO Viesti (aihe, teksti, lähettäjä, aika) VALUES (4, 'huhuu', 'Klaus', Datetime('now)");
        lista.add("INSERT INTO Viesti (aihe, teksti, lähettäjä, aika) VALUES (4, 'kuuleeko kukaan', 'K', Datetime('now)");

        return lista;
    }
}
