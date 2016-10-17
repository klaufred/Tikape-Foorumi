
package foorumi.domain;


public class Aihe {
    private int id;
    private String teksti;
    private int alue_id;
    
    public Aihe(int id, String teksti,  int alue_id) {
        this.alue_id = alue_id;
        this.id = id;
        this.teksti = teksti;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getTeksti() {
        return this.teksti;
    }
    
    public int getAlueId() {
        return this.alue_id;
    }
}
