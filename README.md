# Simple TODO List

La web applcation implementata mette a disposizione una **"TO-DO list"**, ovvero un'applicazione che permette di gestire una serie di attività (stringhe descrittive) e di indicare il completamento di queste ultime.

## Descrizione dell'applicazione

L'applicazione si presenta con un'interfaccia adattiva ottenuta dal template base di Vaadin. Questa è composta da quattro parti principali:
- Menu principale
- Sezione informativa
- Filtri e Azioni
- Visualizzazione della attivita

La prima parte mette a disposzizione dell'utente un menu a cascata formato dalle seguenti voci:
- All
- Today
- Next 7 Days
- Projects
- Status
-- Done
-- Pending

###### Menu principale
Cliccando su ognuna di queste voci nella parte di visualizzazione delle attività appaiono tutte le informazioni opportunamente filtrate. Ad esempio cliccando su "Today" solo le issue relative ad oggi vengono visualizzate, cliccando su "Next 7 Days" vengono visualizzate tutte le attivita per i prossimi 7 giorni. Inoltre è anche possibile filtrare tra le attività completate oppure no, attraverso le sottovoci del menu Status (Done e Pending). La sezione relativa ai progetti non è stata implementata per questa demo. Quando l'utente accede all'applicazione e non trova nessuna attività in corso il sistema lo avvisa con un messaggio che è necessario aggiungere un elemento per visualizzare dei contenuti "There are not Activites in your TODO List , please add one".

###### Sezione informativa
E' popolata attraverso un file .md e permette di fornire all'utente una scheda di help per capire le funzionalità dell'applicativo

###### Filtri e Azioni
La terza sezione mette a disposizione alcunui Filtri ed alcue azioni che l'utente può applicare.
Il filtro che si è deciso di mettere a diposizione va a controllare il match tra il campo inserito dall'utente ed il valore presente nella descrizione dell'attivita.
Le azioni che possono essere effettuate sono tre:
- Aggiunta nuova Attività
- Modifica di una singola Attività
- Rimozione di un insieme di Attività

Attraverso il pulsante rappresentato dall'icona "+" l'utente può inserire una nuova attività. Premuto questo pulsante appare una finestra modale dove viene chiesto all'utente di inserire i seguenti campi:
- Descrizione
- Progetto
- Priorità
- Scadenza
- Stato

Unico campo richiesto obbligatoriamente è il campo della descrizione. Una volta popolato il contenuto può essere salvato attraverso il pulsante Save (Che divenata attivo solo se il campo obbligatorio e popolato) oppure scartato attraverso il pulsante Cancel.
Attarverso il pulsante rappresentato da una matita l'utente può modificare una delle attività visualizzate (per essere attivabile però una ed una sola delle attività deve essere selizionata nella checjbox). La schermata di modifica è simile a quella di immissione appena descritta. In questa sezione l'utenet può selezionare anche lo stato dell'attività cambiando il valore dello switch in fondo all'interfaccia.
L'ultimo pulsante presente in questa sezione è il pulsante per la cancellazione, premendo il quale l'utente può eliminare le entità selezionate. Inoltre prima di rimuovere definitivamente le selezioni all'utenete viene visualizzato un ulteriore warning "Are you sure you want to delete the entry?". Premendo su "OK" la rimozione è definitiva.

## Descrizione tecnica dell'applicazione

L'applicazione è stata realizzata partendo da [Spring Boot](http://projects.spring.io/spring-boot/) per velocizzare i tempi di configurazione e start-up del progetto. Per realizzare l'applicazione si è scelto di utilizzare come tecnologie di base [Spring Data JPA](http://projects.spring.io/spring-data-jpa/) and [Vaadin](https://vaadin.com). Anche se Vaadin non fornisce un modello MVC ben definito utilizzando JPA ed [Hibernate](http://http://hibernate.org/) si è arrivati ad ottenere una versione derivata. Come Database per questo sistema si è deciso di utilizzare come database [H2](http://www.h2database.com/html/main.html). Come ambiente di sviluppo si è utilizzato [Eclipse Neon](http://www.eclipse.org/neon/)

####
Data la semplicità dell'applicazione si è deciso di utilizzare una singola tabella nel database **attivita** composta dai seguenti campi

| id_attivita | descrizione |priorita|progetto|scadenza|stato  |
|-------------|-------------|--------|--------|--------|-------|
|integer      |string	    |string	 |string  | date   |boolean|

Se si usasse eclipse come IDE utilizzando le configurazioni presenti in questo repository è possibile accedere al DB direttamente da questo link ``http://127.0.0.1:8080/h2-console/`` 

## Installazione e utilizo della demo

### Metodo consigliato

* Clonare il progetto
* Importarlo nell'IDE (Eclipse)
* Eseguire il metodo principale dalla Application class (Usare ad esempio Run As -> Spring Boot App)

### Eseguirlo con maven it with maven

```
git clone https://github.com/attybro/activity-vaadin-crud.git
cd activity-vaadin-crud
mvn spring-boot:run
```

### Creae il pacchetto ed eseguirlo

The built .jar file is auto-runnable, so as you can move it to any computer having java installed and run the app.

```
git clone https://github.com/attybro/activity-vaadin-crud.git
cd activity-vaadin-crud
mvn package
java -jar target/activity-vaadin-crud-0.0.1-SNAPSHOT.jar
```

### Effettuare il deploy su tomcat

```
git https://github.com/attybro/activity-vaadin-crud.git
cd activity-vaadin-crud
mvn install

```
<!--
Release Number	: 0.1
Release Date	: 2016/08/05
Release Author	: a.broglio
Release Note	: Prima release
-->