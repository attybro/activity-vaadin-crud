package crud.backend;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Attivita implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id_attivita;

    @NotNull(message = "Description is required")
    @Size(min = 1, max = 50, message = "name must be longer than 3 and less than 40 characters")
    private String descrizione;
    
    private String progetto;
    private String priorita;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date scadenza;
   
    @Column(columnDefinition="boolean default false")   
    private Boolean stato;
    
    public Attivita() {
    }

    public long getId() {
        return id_attivita;
    }

    public void setId(long id_attivita) {
        this.id_attivita = id_attivita;
    }

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getProgetto() {
        return progetto;
    }

    public void setProgetto(String progetto) {
        this.progetto = progetto;
    }

    public String getPriorita() {
        return priorita;
    }

    public void setPriorita(String priorita) {
        this.priorita = priorita;
    }

    public Boolean getStato() {
        return stato;
    }

    public void setStato(Boolean stato) {
    	this.stato = stato;
    }
}
