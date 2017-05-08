package crud.vaadin;

import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import crud.backend.Attivita;
import crud.backend.AttivitaRepository;
import org.vaadin.spring.events.EventBus;
import org.vaadin.teemu.switchui.Switch;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@UIScope
@SpringComponent
public class AttivitaForm extends AbstractForm<Attivita> {

    private static final long serialVersionUID = 1L;

    EventBus.UIEventBus eventBus;
    AttivitaRepository repo_att;

    TextField 	descrizione	= new MTextField("Descrizione");
    TextField 	progetto	= new MTextField("Progetto");
    NativeSelect<String> priorita = new NativeSelect<>("Priorità");
    DateField 	scadenza	= new DateField("Scadenza");
    Switch		stato		= new Switch("Stato");
    
    
    AttivitaForm(AttivitaRepository r, EventBus.UIEventBus b) {
        super(Attivita.class);
        this.repo_att = r;
        this.eventBus = b;
        priorita.setItems("High","Medium","Low");
        /*Pubblica eventi al salvataggio cancellazione per le altre parti della UI*/
        setSavedHandler(attivita -> {
            /*Salva il valore*/
            repo_att.save(attivita);
            /*Pubblica l'evento*/
            eventBus.publish(this, new AttivitaModifiedEvent(attivita));
        });
        setResetHandler(p -> eventBus.publish(this, new AttivitaModifiedEvent(p)));

        setSizeUndefined();
    }

    @Override
    protected void bind() {
        /*Conversione della data per compatibilità*/
        getBinder().forMemberField(scadenza).withConverter(new LocalDateToDateConverter());
        
        super.bind();
    }
    
    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                		descrizione,
                		progetto,
                		priorita,
                		scadenza,
                		stato
                ).withWidth(""),
                getToolbar()
        ).withWidth("");
    }
}
