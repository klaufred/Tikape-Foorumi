
package foorumi.database;

import foorumi.domain.Aihe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class AiheDao implements Dao<Aihe, Integer>{
    Database database;
    
    public AiheDao(Database base) {
        this.database = base;
    }

    @Override
    public Aihe etsiYksi(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihe WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        
        if (!rs.next()) {
            rs.close();
            stmt.close();
            return null;
        }

        Integer id = rs.getInt("id");
        String teksti = rs.getString("teksti");
        Integer alueId = rs.getInt("alue_id");
        

        Aihe aihe = new Aihe(id, teksti, alueId);

        rs.close();
        stmt.close();
        connection.close();

        return aihe;
    }

    @Override
    public List<Aihe> etsiKaikki() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void tallenna(String teksti, Integer alueId) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

}
    
}
