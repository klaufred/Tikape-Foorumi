package foorumi.database;

import foorumi.domain.Aihe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AiheDao implements Dao<Aihe, Integer> {

    Database database;

    public AiheDao(Database base) {
        this.database = base;
    }

    @Override
    public Aihe etsiYksi(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihe WHERE aihe_id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            rs.close();
            stmt.close();
            return null;
        }

        Integer id = rs.getInt("aihe_id");
        String teksti = rs.getString("teksti");
        Integer alueId = rs.getInt("alue");

        Aihe aihe = new Aihe(id, teksti, alueId);

        rs.close();
        stmt.close();
        connection.close();

        return aihe;
    }

    public List<Aihe> etsiKaikki(Integer alue) throws SQLException {
        Connection conn = database.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM Aihe WHERE alue = " + alue);

        List<Aihe> aiheet = new ArrayList<>();

        while (result.next()) {
            int id = result.getInt("aihe_id");
            String teksti = result.getString("teksti");
            int alue_id = result.getInt("alue");

            Aihe aihe = new Aihe(id, teksti, alue_id);
            aiheet.add(aihe);
        }

        conn.close();

        return aiheet;

    }

    public void tallenna(String teksti, String alueId) throws SQLException {
        Connection conn = database.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO Aihe (teksti, alue_id) "
                + "VALUES ('" + teksti + "', '" + Integer.parseInt(alueId) + "')");

        conn.close();
    }

    public int getId(String id) throws SQLException {
        Connection conn = database.getConnection();
        Statement stmt = conn.createStatement();

        ResultSet result = stmt.executeQuery("SELECT * FROM Todo WHERE id = " + id);

        return result.getInt("alue_id");
    }

}
