package com.example.vs.Repository;

import com.example.vs.Entity.Ticket;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Modifying
    @Query(value = "update tickets set status = 'Sent' where id = ?1", nativeQuery = true)
    void sendTicket(long id);

    @Modifying
    @Query(value = "update tickets set status = 'Accepted' where id = ?1", nativeQuery = true)
    void acceptedTicket(long id);

    @Modifying
    @Query(value = "update tickets set status = 'Rejected' where id = ?1", nativeQuery = true)
    void rejectedTicket(long id);

    @Query(value = "select * from tickets where status = 'Sent'", nativeQuery = true)
    List<Ticket> findAllEditor(PageRequest pageRequest);

    List<Ticket> findAllByAuthor(long id);

    @Query(value = "select * from tickets where author = ?1 AND status ='Sent' order by created_time DESC limit ?2 offset 5*?3", nativeQuery = true)
    List<Ticket> findAllMyTicketsDESC(long id, int size, int page);

    @Query(value = "select * from tickets where author = ?1 AND status ='Sent' order by created_time ASC limit ?2 offset 5*?3", nativeQuery = true)
    List<Ticket> findAllMyTicketsASC(long id, int size, int page);

    @Query(value = "select * from tickets where author = ?1 order by created_time DESC limit ?2 offset 5*?3", nativeQuery = true)
    List<Ticket> findEditorDESC(long id, int size, int page);

    @Query(value = "select * from tickets where author = ?1 order by created_time ASC limit ?2 offset 5*?3", nativeQuery = true)
    List<Ticket> findEditorACS(long id, int size, int page);


}
