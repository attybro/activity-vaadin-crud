package crud.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import crud.backend.Attivita;
import crud.backend.AttivitaRepository;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang3.time.DateUtils;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.components.DisclosurePanel;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.RichText;


@Title("ToDo List CRUD")
@Theme("valo")
@SpringUI
public class MainUI extends UI {

    private static final long serialVersionUID = 1L;
    AttivitaRepository	repo_att;
    AttivitaForm		attivitaForm;
    EventBus.UIEventBus eventBus;    
    private Grid<Attivita> grid		= new Grid<>(Attivita.class);

    private MTextField filterByName = new MTextField().withPlaceholder("Filter By Descrizione");
    private Button addNewAtt	= new MButton(VaadinIcons.PLUS, this::addAtt);
    private Button editAtt		= new MButton(VaadinIcons.PENCIL, this::editAtt);

    private Button deleteAtt	= new ConfirmButton(VaadinIcons.TRASH,"Are you sure you want to delete the entry?", this::removeAtt);
    private Label noAttivita= new Label("There are not Activites in your TODO List " + VaadinIcons.MAILBOX.getHtml() + ", please add one. ", ContentMode.HTML);

    
    public MainUI(AttivitaForm f_att, EventBus.UIEventBus b, AttivitaRepository r_att) {
        this.repo_att		= r_att;
        this.attivitaForm	= f_att;
        this.eventBus 		= b;
    }

    @Override
    protected void init(VaadinRequest request) {
    	final VerticalLayout layout = new VerticalLayout();
    	Responsive.makeResponsive(this);
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);
        filterByName.addValueChangeListener(e -> {
            listActivities(e.getValue());
        });
        DisclosurePanel aboutBox = new DisclosurePanel("Activities List example developed with Vaadin UI, Spring Hibernate", new RichText().withMarkDownResource("/welcome.md"));       
      
        /*Inserrimento di un semplice menu a cascata*/
        MenuBar	topMenu				= new MenuBar();
        topMenu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        Label labelMenu = new Label("All");
        labelMenu.setSizeFull();
        
        HorizontalLayout topMenuEl	= new HorizontalLayout(topMenu);
        topMenuEl.addComponents(topMenu,labelMenu);
        MenuItem topMenuItems		= topMenu.addItem("",VaadinIcons.MENU,null);
        
        MenuBar.Command mycommand = new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
            	labelMenu.setValue(selectedItem.getText());
            	filterByName.setValue("");
            	filterByName.clear();         	
            	switch(selectedItem.getText()) {

            	case "Today":
            		listTodayActivities();
            		break;
            	case "Done":
            		listStatusActivities(true);
            		break;
            	case "Pending":
            		listStatusActivities(false);
            		break;
            	case "Next 7 Days":
            		listFutureActivities();
            		break;
                /*TODO
                 * case "Projects":
                 * listProjectActivities(selectedItem.getText());
                 * break;
                 */            	
            	default:
            		listActivities();
            }                
            }
        };
        
        
        topMenuItems.addItem("All",			null,	mycommand );        
        topMenuItems.addItem("Today",		null,	mycommand );
        topMenuItems.addItem("Next 7 Days",	null,	mycommand );
        topMenuItems.addItem("Projects",	null,	mycommand );
        
        MenuItem topMenuItemsII = topMenuItems.addItem("Status", null, null);
        topMenuItemsII.addItem("Done",		null,	mycommand );
        topMenuItemsII.addItem("Pending",	null,	mycommand );    
        HorizontalLayout topPart = new HorizontalLayout(topMenu,labelMenu);
        layout.addComponents(topPart);
        
        /*IInserisci le colonne visualizzabili nella grid*/
        grid.setColumns("descrizione","progetto","priorita","scadenza","stato");
        /*Eventualmente riordina le colonne della grid*/
        //grid.setColumnOrder()
        grid.getColumn("progetto").setHidden(true);
        
        HorizontalLayout todoList = new HorizontalLayout(grid,noAttivita);
        todoList.setSizeFull();
        grid.setSizeFull();               
        grid.getColumn("stato").setCaption("Done");
        noAttivita.setVisible(false);
        HorizontalLayout mdInfo = new HorizontalLayout(aboutBox);        
        layout.addComponents(mdInfo);        
        HorizontalLayout actionList = new HorizontalLayout(filterByName,addNewAtt, editAtt, deleteAtt);        
        layout.addComponents(actionList,todoList);
        listActivities();

        grid.setSelectionMode(SelectionMode.MULTI);
		grid.addSelectionListener(event -> {
		    Set<Attivita> selected = event.getAllSelectedItems();
		    Notification.show(selected.size() + " items selected");
		    adjustActionButtonState();
		});
		        
        setContent(layout);
        eventBus.subscribe(this);
    }

    /*Pilota la logica per la visibilità dei pulsanti nella parte superiore*/
    protected void adjustActionButtonState() {
        boolean hasSelection = !grid.getSelectedItems().isEmpty();
        if (grid.getSelectedItems().size()==1 && hasSelection)
        	editAtt.setEnabled(true);
        else
        	editAtt.setEnabled(false);
        deleteAtt.setEnabled(hasSelection);
    }

    /*Recupero dei dati dal modello di backend*/
    private void listActivities() {
    	listActivities(filterByName.getValue());
    }
    
    private void listActivities(String descrizioneFilter) {     
    	String likeFilter = "%" + descrizioneFilter + "%";
        if (repo_att.findByDescrizioneLikeIgnoreCase(likeFilter).isEmpty()){
        	noAttivita.setVisible(true);
        	grid.setVisible(false);
        }else{
        	noAttivita.setVisible(false);
        	grid.setVisible(true);	
        }
        grid.setItems(repo_att.findByDescrizioneLikeIgnoreCase(likeFilter));
        adjustActionButtonState();
    }

    private void listTodayActivities() {     
    	Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        if (repo_att.findByScadenza(today).isEmpty()){
        	noAttivita.setVisible(true);
        	grid.setVisible(false);
        }else{
        	noAttivita.setVisible(false);
        	grid.setVisible(true);	
        }
        grid.setItems(repo_att.findByScadenza(today));
        adjustActionButtonState();
    }
    
    private void listFutureActivities() {     
    	Date today	= DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
    	Date dt = new Date();
    	Calendar c = Calendar.getInstance(); 
    	c.setTime(dt); 
    	c.add(Calendar.DATE, 7);
    	dt = c.getTime();
    	Date endDay = DateUtils.truncate(dt, Calendar.DAY_OF_MONTH);
        if (repo_att.findByScadenzaBetween(today,endDay).isEmpty()){
        	noAttivita.setVisible(true);
        	grid.setVisible(false);
        }else{
        	noAttivita.setVisible(false);
        	grid.setVisible(true);	
        }
        grid.setItems(repo_att.findByScadenzaBetween(today,endDay));
        adjustActionButtonState();
    }
    
    private void listStatusActivities(Boolean actualStatus) {      
        if (repo_att.findByStato(actualStatus).isEmpty()){
        	noAttivita.setVisible(true);
        	grid.setVisible(false);
        }else{
        	noAttivita.setVisible(false);
        	grid.setVisible(true);	
        }
        grid.setItems(repo_att.findByStato(actualStatus));
        adjustActionButtonState();
    }
    
    
/***********************************************************/
    public void addAtt(ClickEvent clickEvent) {
        editAtt(new Attivita());
    }

    public void editAtt(ClickEvent e) {
        for (Attivita itemId: grid.getSelectedItems())
        	editAtt(itemId);   	
    }

    public void removeAtt() {
        for (Attivita itemId: grid.getSelectedItems())
            repo_att.delete(itemId);
        grid.deselectAll();
        listActivities();
    }

    protected void editAtt(final Attivita nuovaAttivita) {
        attivitaForm.setEntity(nuovaAttivita);
        attivitaForm.openInModalPopup();
    }
    
    @EventBusListenerMethod(scope = EventScope.UI)
    public void onAttivitaModified(AttivitaModifiedEvent event) {
        listActivities();
        attivitaForm.closePopup();
    }

}
