package foorumi.database;

import foorumi.domain.AiheenViestit;
import foorumi.domain.Aihe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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

    public void tallenna(String teksti, Integer alueId) throws SQLException {
        Connection conn = database.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO Aihe (alue, teksti) "
                + "VALUES ('" + alueId + "', '" + teksti  + "')");

        stmt.close();
        conn.close();
    }

    public int getId(String id) throws SQLException {
        Connection conn = database.getConnection();
        Statement stmt = conn.createStatement();

        ResultSet result = stmt.executeQuery("SELECT * FROM Todo WHERE id = " + id);

        return result.getInt("alue_id");
    }

    public List<AiheenViestit> haeAiheet(Integer alue_id) throws SQLException {
        Connection connection = this.database.getConnection();
        List<AiheenViestit> aiheet = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement(""
            + "SELECT Aihe.aihe_id, Aihe.teksti, "
            + "COUNT(Viesti.viesti_id) AS viestit "
            + "FROM Aihe "
            + "LEFT JOIN Viesti ON Viesti.aihe = Aihe.aihe_id "
            + "WHERE Aihe.alue = " + alue_id
            + " GROUP BY Aihe.aihe_id "
            + "ORDER BY Viesti.aika DESC "
            + "LIMIT 10;"); 
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Integer aihe_id = rs.getInt("aihe_id");
                String teksti = rs.getString("teksti");
                Integer viestienMaara = rs.getInt("viestit"); 

                PreparedStatement stmt1 = connection.prepareStatement(""
                    + "SELECT Viesti.aika FROM Viesti, Aihe, Alue "
                    + "WHERE Viesti.aihe = Aihe.aihe_id "
                    + "AND Aihe.alue = Alue.alue_id "
                    + "AND Alue.alue_id = " + alue_id
                    + " AND Aihe.aihe_id = " + aihe_id
                    + " ORDER BY Viesti.viesti_id DESC LIMIT 1");

                ResultSet rs1 = stmt1.executeQuery();

                String viimeisinAika = null;
                if (rs1.next()) {
                    viimeisinAika = rs1.getString("aika");
                }
                rs1.close();
                stmt1.close();  
                aiheet.add(new AiheenViestit(aihe_id, teksti, viestienMaara, viimeisinAika));
            }
            
        rs.close();
        stmt.close();
        connection.close();
        return aiheet;
    }

}
