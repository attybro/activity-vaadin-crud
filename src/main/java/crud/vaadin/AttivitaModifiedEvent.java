package crud.vaadin;
import crud.backend.Attivita;

import java.io.Serializable;

public class AttivitaModifiedEvent implements Serializable {
	
	private final Attivita attivita;

    public AttivitaModifiedEvent(Attivita p) {
        this.attivita = p;
    }

    public Attivita getAttivita() {
        return attivita;
    }
    
}