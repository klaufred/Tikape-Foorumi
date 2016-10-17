
package foorumi.domain;

public class Alue {
    private Integer id;
    private String nimi;
    
    public Alue(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public String getNimi() {
        return this.nimi;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String nimi) {
        this.nimi = nimi;
    }
}
