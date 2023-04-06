package com.example.vs.Services;

import com.example.vs.Entity.Ticket;
import com.example.vs.Repository.TicketRepository;
import com.example.vs.Util.Status;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional
    public Ticket saveTicket(Ticket ticket){
        ticket.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        ticket.setStatus(String.valueOf(Status.Draft));
        return ticketRepository.save(ticket);
    }

    @Transactional
    public void sendTicket(long id){
        ticketRepository.sendTicket(id);
    }

    @Transactional
    public void acceptedTicket(long id){
        ticketRepository.acceptedTicket(id);
    }

    @Transactional
    public void rejectedTicket(long id){
        ticketRepository.rejectedTicket(id);
    }

    public List<Ticket> userTickets(long id){
        return ticketRepository.findAllByAuthor(id);
    }

    public List<Ticket> userTicketsWithSortAndPagination(PageRequest pageRequest){
       Page<Ticket> tickets = ticketRepository.findAll(pageRequest);
        return tickets.getContent();
    }

    public List<Ticket> userTicketsEditor(PageRequest pageRequest){
        Page<Ticket> tickets = (Page<Ticket>) ticketRepository.findAllEditor(pageRequest);
        return tickets.getContent();
    }

    public List<Ticket> AllMyTickets(long id, String sort, int size, int page){
        if (sort.equals("DESC")) {
            return ticketRepository.findAllMyTicketsDESC(id, size, page);
        }
        else{
            return ticketRepository.findAllMyTicketsASC(id, size, page);
        }
    }
    public List<Ticket> findByNameForEditor(long id, String sort, int size, int page){
        if (sort.equals("DESC")) {
            return ticketRepository.findEditorDESC(id, size, page);
        }
        else{
            return ticketRepository.findEditorACS(id, size, page);
        }
    }


}
