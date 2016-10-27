package foorumi.domain;

public class AlueenViestit {
    private Integer id;
    private String nimi;
    private String aika;
    private Integer viestit;
    
    public AlueenViestit (Integer id, String nimi, Integer viestit, String aika) {
        this.id = id;
        this.nimi = nimi;
        this.viestit = viestit;
        this.aika = aika;
    } 
    
    public String getNimi() {
        return nimi;
    }

    public Integer getViestit() {
        return viestit;
    }
    
    public String getAika() {
        return aika;
    }

    public Integer getId() {
        return this.id;
    }
}