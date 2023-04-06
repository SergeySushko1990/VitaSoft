package com.example.vs.Controllers;

import com.example.vs.DTO.TicketDTO;
import com.example.vs.Entity.Ticket;
import com.example.vs.Exceptions.TicketErrorResponse;
import com.example.vs.Exceptions.TicketException;
import com.example.vs.Repository.TicketRepository;
import com.example.vs.Services.TicketService;
import com.example.vs.Services.UsersService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/tickets")
@AllArgsConstructor
public class TicketsController {

    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    private final UsersService usersService;


    @PostMapping("/new")
    public ResponseEntity<HttpStatus> createTicket(@RequestBody Ticket ticket, Principal principal) {
        ticket.setAuthor(usersService.userIdInfo(principal));
        ticketService.saveTicket(ticket);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    //Все заявки (только для администраторов)
    @GetMapping("/all/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TicketDTO> allTickets(@RequestParam(value = "size", defaultValue = "5", required = false) int size,
                                   @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                   @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {
        if (sort.equals("ASC") == true){ sort = "ASC";}
        else {sort = "DESC";}
        return ticketService.userTicketsWithSortAndPagination(PageRequest.of(page, size, Sort.Direction.valueOf(sort), "createdTime")).stream().map(this::convertToTicketDTO)
                .collect(Collectors.toList());
    }

    //Все заявки (только для администраторов)
    @GetMapping("/allEditor/")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public List<TicketDTO> allTicketsEditor(@RequestParam(value = "size", defaultValue = "5", required = false) int size,
                                      @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort) {
        if (sort.equals("ASC") == true){ sort = "ASC";}
        else {sort = "DESC";}
        return ticketService.userTicketsEditor(PageRequest.of(page, size, Sort.Direction.valueOf(sort), "createdTime")).stream().map(this::convertToTicketDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/sendticket/")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<HttpStatus> sendTicket(Principal principal, @RequestParam(value = "id", required = true) long id){
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if(usersService.userIdInfo(principal) == ticket.get().getAuthor()) {
        ticketService.sendTicket(id);
        return ResponseEntity.ok(HttpStatus.OK);}
        else {
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/acceptedticket/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public ResponseEntity<HttpStatus> acceptedTicket(Ticket ticket, @PathVariable("id") int id){
        ticketService.acceptedTicket(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/rejectedticket/")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
    public ResponseEntity<HttpStatus> rejectedTicket(Ticket ticket,
                                                     @RequestParam(value = "id", required = true) long id){
        ticketService.rejectedTicket(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public List<TicketDTO> userTickets(@PathVariable("id") long id){
        return ticketService.userTickets(id).stream().map(this::convertToTicketDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/edit/")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<HttpStatus> updateTicket(@RequestBody Ticket ticket, Principal principal, @RequestParam(value = "id", required = true) long id){
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        Long userId = usersService.userIdInfo(principal);
        Long userID = optionalTicket.get().getAuthor();
        if (optionalTicket.isEmpty()) {
            return (ResponseEntity<HttpStatus>) ResponseEntity.notFound();
        }
        if (optionalTicket.get().getStatus().equals("Draft") == true & userId == userID) {
            ticket.setId(id);
            ticket.setAuthor(userID);
            ticketService.saveTicket(ticket);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        else {
            return  ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }
        }

        @GetMapping("/mytickets")
        @PreAuthorize("hasRole('ROLE_USER')")
        public List<TicketDTO> myTickets(@RequestParam(value = "size", defaultValue = "5", required = false) int size,
                                      @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                      @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort, Principal principal){
        if (sort.equals("ASC") == true){ sort = "ASC";}
        else {sort = "DESC";}
        long id = usersService.userIdInfo(principal);
        return ticketService.AllMyTickets(id, sort, size, page).stream().map(this::convertToTicketDTO)
                .collect(Collectors.toList());
        }

        @GetMapping("/ticketsForEditor")
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
        public List<TicketDTO> ticketsEditor(@RequestParam(value = "size", defaultValue = "5", required = false) int size,
                                  @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                  @RequestParam(value = "sort", defaultValue = "DESC", required = false) String sort,
                                          @RequestParam(value = "name", defaultValue = "", required = false) String name){
        if (sort.equals("ASC") == true){ sort = "ASC";}
        else {sort = "DESC";}
        long id = usersService.findUserByName(name);
        return ticketService.findByNameForEditor(id, sort, size, page).stream().map(this::convertToTicketDTO)
                .collect(Collectors.toList());
    }

    private Ticket condertToTicket(TicketDTO ticketDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(ticketDTO, Ticket.class);
    }

    private TicketDTO convertToTicketDTO(Ticket ticket){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(ticket, TicketDTO.class);
    }
}

