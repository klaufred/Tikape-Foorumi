
package foorumi.database;

import java.sql.SQLException;
import java.util.List;

    public interface Dao<T, K> {

    T etsiYksi(K key) throws SQLException;

    List<T> etsiKaikki() throws SQLException;
}

