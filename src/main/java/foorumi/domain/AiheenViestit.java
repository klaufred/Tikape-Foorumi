
package foorumi.domain;


public class AiheenViestit {
    private Integer id;
    private String teksti;
    private String aika;
    private Integer viestit;

    public AiheenViestit(Integer aihe_id, String teksti, Integer viestienMaara, String viimeisinAika) {
        this.id = aihe_id;
        this.teksti = teksti;
        this.viestit = viestienMaara;
        this.aika = viimeisinAika;
    } 
    
    public String getTeksti() {
        return this.teksti;
    }

    public Integer getViestit() {
        return this.viestit;
    }
    
    public String getAika() {
        return this.aika;
    }

    public Integer getId() {
        return this.id;
    }
}
