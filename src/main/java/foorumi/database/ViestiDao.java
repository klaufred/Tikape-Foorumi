
package foorumi.database;

import foorumi.domain.Viesti;
import java.sql.SQLException;
import java.util.List;


public class ViestiDao implements foorumi.database.Dao<Viesti, Integer> {

    public ViestiDao(Database database) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Viesti etsiYksi(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Viesti> etsiKaikki() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
