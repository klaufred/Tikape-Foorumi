
package foorumi.database;

import foorumi.domain.Viesti;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ViestiDao implements foorumi.database.Dao<Viesti, Integer> {
    private Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }
    
    public void tallenna(String teksti, String lahettaja, String aihe) throws SQLException {
        
        Connection connection = this.database.getConnection();
        Statement stmt = connection.createStatement();
        stmt.execute("INSERT INTO Viesti (aihe, teksti, lähettäjä, aika) "
                + "VALUES ('" + Integer.parseInt(aihe) + "', '" + teksti + "', '" + lahettaja + "', '" + Timestamp.valueOf(LocalDateTime.MIN) + "')");
        stmt.close();
        connection.close();
        
        
}

    @Override
    public Viesti etsiYksi(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE id = ?");
        stmt.setObject(1, key);
        
        ResultSet rs = stmt.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            stmt.close();
            return null;
        }
            Integer id = rs.getInt("viesti_id");
            String teksti = rs.getString("teksti");
            Timestamp aika = rs.getTimestamp("aika");
            String lahettaja = rs.getString("lähettäjä");
            Integer aihe_id = rs.getInt("aihe");

            return new Viesti(id, teksti, lahettaja, aihe_id, aika);
    }

    @Override
    public List<Viesti> etsiKaikki(Integer aihe) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE aihe = " + aihe);
        
        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        
        while (rs.next()) {
            Integer id = rs.getInt("viesti_id");
            String teksti = rs.getString("teksti");
            Timestamp aika = rs.getTimestamp("aika");
            String lahettaja = rs.getString("lähettäjä");
            Integer aihe_id = rs.getInt("aihe");

            viestit.add(new Viesti(id, teksti, lahettaja, aihe_id, aika));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }
    
    public List<Viesti> etsiKymmenenUusinta(Integer aihe) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE aihe = " + aihe + " ORDER BY aika DESC LIMIT 10;");
        
        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        
        while (rs.next()) {
            Integer id = rs.getInt("viesi_id");
            String teksti = rs.getString("teksti");
            Timestamp aika = rs.getTimestamp("aika");
            String lahettaja = rs.getString("lähettäjä");
            Integer aihe_id = rs.getInt("aihe");

            viestit.add(new Viesti(id, teksti, lahettaja, aihe_id, aika));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }
    
}
