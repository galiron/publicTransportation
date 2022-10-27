package de.fhkiel.advancedjava.restservice;

import de.fhkiel.advancedjava.businesslogic.TicketLogic;
import de.fhkiel.advancedjava.dto.TicketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = API.TICKET)
public class TicketController {

    private final TicketLogic ticketLogic;

    @Autowired
    public TicketController(TicketLogic ticketLogic) {
        this.ticketLogic = ticketLogic;
    }

    /**
     * Inserts bought tickets in the Database.
     *
     * @param tickets The Format the JsonFile should be based on.
     * @return ResponseEntity with generated ticket as body (if successful).
     */
    @PutMapping(path = API.BUY, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<List<TicketDTO>> buyTickets(@RequestBody List<TicketDTO> tickets) {
        try {
            return ResponseEntity.ok(ticketLogic.buyTickets(tickets));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
