
package foorumi.runko;

import foorumi.database.AiheDao;
import foorumi.database.AlueDao;
import foorumi.database.Database;
import foorumi.database.ViestiDao;
import foorumi.domain.Alue;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Tekstikayttoliittyma {

    private Scanner lukija;
    private AlueDao aluedao;
    private ViestiDao viestidao;
    private AiheDao aihedao;

    public Tekstikayttoliittyma() throws ClassNotFoundException, Exception {
        Database database = new Database("foorumi.db");

        database.init();
        aluedao = new AlueDao(database);
        aihedao = new AiheDao(database);
        viestidao = new ViestiDao(database);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Kirjoita minkä halua testata : Alue, Aihe, Viesti");
        String kysymys = scanner.nextLine();
        
        if (kysymys.equals("Alue")) {
            try {
                System.out.println("Haetaan ensin kaikki");
                List<Alue> lista = aluedao.etsiKaikki();
                for (Alue a : lista) {
                    System.out.println(a.getNimi());
                }}  catch (SQLException ex) {
                    System.out.println("Epäonnistui");
            }
                
            try {
                System.out.println("Kokeillaan tallentamista:");
                System.out.println("Anna nimi");
                String nimi = scanner.nextLine();
                aluedao.tallenna(nimi);
                System.out.println("Onnistui");
                } catch (SQLException ex) {
                    System.out.println("Epäonnistui");
            }   
            
            try {
                System.out.println("Kokeillaan hakemista:");
                System.out.println("Anna id");
                String id = scanner.nextLine();
                int id2 = Integer.parseInt(id);
                
                System.out.println(aluedao.etsiYksi(id2).getNimi());
                } catch (SQLException ex) {
                    System.out.println("Epäonnistui");
            }      
            
        }
            
            
            
        
        
        if (kysymys.equals("Viesti")) {
            
        }
        
        if (kysymys.equals("Aihe")) {
            
        }
    }
}