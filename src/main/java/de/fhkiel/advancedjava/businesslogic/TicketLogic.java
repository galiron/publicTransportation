package de.fhkiel.advancedjava.businesslogic;

import de.fhkiel.advancedjava.dto.TicketDTO;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Ticket;
import de.fhkiel.advancedjava.exceptions.MissingNodeException;
import de.fhkiel.advancedjava.generator.DTOGenerator;
import de.fhkiel.advancedjava.generator.TicketGenerator;
import de.fhkiel.advancedjava.repository.SectionRepository;
import de.fhkiel.advancedjava.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.IllegalTransactionStateException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TicketLogic {

    private final TicketRepository ticketRepository;
    private final SectionRepository sectionRepository;

    /**
     * Creates tickets in the repository.
     *
     * @param tickets Tickets that shall be created.
     * @return List of tickets that have been created.
     * @throws MissingNodeException Gets thrown if a section is invalid (missing a node)
     */
    public List<TicketDTO> buyTickets(List<TicketDTO> tickets) throws MissingNodeException {
        List<Ticket> ticketsToSave = new ArrayList<>();

        tickets.forEach(
                ticketDTO -> {
                    Optional<Section> section = sectionRepository.findById(ticketDTO.getSection().getSectionId());
                    if (section.isEmpty()) throw new IllegalArgumentException();
                    ticketsToSave.add(TicketGenerator.customTicket(null
                            , ticketDTO.getPrice()
                            , section.get()));
                }
        );

        List<TicketDTO> ticketsToReturn = new ArrayList<>();
        try {
            Iterable<Ticket> ticketsFromDB = ticketRepository.saveAll(ticketsToSave);
            ticketsFromDB.forEach(ticket -> ticketsToReturn.add(DTOGenerator.ticketDTOFromTicket(ticket)));
        } catch (IllegalArgumentException e) {
            throw new MissingNodeException("section could not be found by given ID!");
        } catch (Exception e) {
            throw new IllegalTransactionStateException("tickets could not be saved");
        }
        return ticketsToReturn;
    }
}
