
package foorumi.database;

import foorumi.domain.Viesti;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ViestiDao implements foorumi.database.Dao<Viesti, Integer> {
    private Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }
    
    public void tallenna(String teksti, String lahettaja, Integer alue_id, Date aika) throws SQLException {
        
        Connection connection = this.database.getConnection();
        Statement stmt = connection.createStatement();
        stmt.execute("INSERT INTO Viesti (teksti, lahettaja, aihe_id, aika) "
                + "VALUES ('" + teksti + "', '" + lahettaja + "', '" + alue_id + "', '" + aika + "')");
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
            Integer id = rs.getInt("id");
            String teksti = rs.getString("teksti");
            Date aika = rs.getDate("aika");
            String lahettaja = rs.getString("lahettaja");
            Integer aihe_id = rs.getInt("aihe_id");

            return new Viesti(id, teksti, lahettaja, aihe_id, aika);
    }

    @Override
    public List<Viesti> etsiKaikki() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti");
        
        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String teksti = rs.getString("teksti");
            Date aika = rs.getDate("aika");
            String lahettaja = rs.getString("lahettaja");
            Integer aihe_id = rs.getInt("aihe_id");

            viestit.add(new Viesti(id, teksti, lahettaja, aihe_id, aika));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }
    
}
