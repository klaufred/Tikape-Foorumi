
package foorumi.database;

import foorumi.domain.Alue;
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
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            stmt.close();
            return null;
        }

        Integer id = rs.getInt("id");
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
    
    
    
}
