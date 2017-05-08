
package crud.backend;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AttivitaRepository extends JpaRepository<Attivita, Long> {
    
	/*Recupera tutti i dati dal modello*/
    List<Attivita> findAllBy(Pageable pageable);
    /*Recupera tutti i dati con filtri ad-hoc*/
    List<Attivita> findByDescrizione(String descrizioneFilter);
    List<Attivita> findByDescrizioneLikeIgnoreCase(String descrizioneFilter);
    List<Attivita> findByProgettoLikeIgnoreCase(String projectFilter);
    List<Attivita> findByScadenza(Date scadenzaFilter);
    List<Attivita> findByScadenzaBetween(Date start, Date end);
    List<Attivita> findByStato(Boolean statoFilter);
    /*Recupera tutti i dati con filtri ad-hoc ed eventuale paging*/
    List<Attivita> findByDescrizioneLikeIgnoreCase(String descrizioneFilter, Pageable pageable);
    List<Attivita> findByProgettoLikeIgnoreCase(String projectFilter, Pageable pageable);

    long countByDescrizioneLike(String descrizioneFilter);

}
