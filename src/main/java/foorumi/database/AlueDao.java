
package foorumi.database;

import foorumi.domain.Alue;
import foorumi.domain.AlueenViestit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlueDao implements Dao<Alue, Integer>{
    
    private Database database;

    public AlueDao(Database database) {
        this.database = database;
    }

    @Override
    public Alue etsiYksi(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE alue_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            stmt.close();
            return null;
        }

        Integer id = rs.getInt("alue_id");
        String nimi = rs.getString("nimi");

        Alue alue = new Alue(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return alue;
    }

    @Override
    public List<Alue> etsiKaikki(Integer i) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue");

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("alue_id");
            String nimi = rs.getString("nimi");

            alueet.add(new Alue(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }

    public void tallenna(String nimi) throws SQLException {
        Connection connection = this.database.getConnection();
        Statement stmt = connection.createStatement();
        stmt.execute("INSERT INTO Alue(nimi) VALUES ('" + nimi + "')");
        stmt.close();
        connection.close();
    }
    
    public List<AlueenViestit> haeAiheetJaViestit() throws SQLException {
        Connection connection = this.database.getConnection();
        List<AlueenViestit> alueet = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement("SELECT Alue.alue_id, Alue.nimi, "
                + "COUNT(Viesti.viesti_id) AS viestit "
                + "FROM Alue "
                + "LEFT JOIN Aihe ON Aihe.alue = Alue.alue_id "
                + "LEFT JOIN Viesti ON Viesti.aihe = aihe.aihe_id "
                + "GROUP BY Alue.alue_id "
                + "ORDER BY viesti.aika DESC"); 

        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Integer alue_id = rs.getInt("alue_id");
            String nimi = rs.getString("nimi");
            Integer viestienMaara = rs.getInt("viestit"); 
            
            PreparedStatement stmt1 = connection.prepareStatement("SELECT Viesti.aika "
                    + "FROM Viesti, Aihe, Alue "
                    + "WHERE Viesti.aihe = Aihe.aihe_id "
                    + "AND Aihe.alue = Alue.alue_id "
                    + "AND Alue.alue_id = " + alue_id
                    + " ORDER BY Viesti.viesti_id DESC LIMIT 1");
            
            ResultSet rs1 = stmt1.executeQuery();
            
            String viimeisinAika = "";
            if (rs1.next()) {
                viimeisinAika = rs1.getString("aika");
            }
            
            rs1.close();
            stmt1.close();  
            alueet.add(new AlueenViestit(alue_id, nimi, viestienMaara, viimeisinAika));
        }
        rs.close();
        stmt.close();
        connection.close();
        return alueet;
}
    
}
