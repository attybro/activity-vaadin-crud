CREATE TABLE attivita (
  id_attivita 	bigint auto_increment,
  descrizione	varchar(50) not null,
  progetto		varchar(50) not null,
  priorita		varchar(50) not null,
  scadenza		timestamp,
  stato 		bool default false not null,
  primary key (id_attivita));
