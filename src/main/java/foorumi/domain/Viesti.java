
package foorumi.domain;

import java.sql.Date;


public class Viesti {
    private int id;
    private String teksti;
    private String lahettaja;
    private int aihe_id;
    private Date aika;
    
    public Viesti(int id, String teksti, String lahettaja, int aihe_id, Date aika) {
        this.aihe_id = aihe_id;
        this.id = id;
        this.lahettaja = lahettaja;
        this.teksti = teksti;
        this.aika = aika;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getLahettaja() {
        return this.lahettaja;
    }
    
    public String getTeksti() {
        return this.teksti;
    }
    
    public Date getAika() {
        return this.aika;
    }
    
    
}
