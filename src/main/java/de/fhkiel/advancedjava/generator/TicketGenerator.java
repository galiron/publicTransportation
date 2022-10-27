package de.fhkiel.advancedjava.generator;

import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Ticket;

public class TicketGenerator {

    /**
     * Creates a Ticket with custom parameters
     *
     * @param id      id
     * @param price   price
     * @param section section
     * @return Ticket
     */
    public static Ticket customTicket(Long id, Double price, Section section) {
        Ticket ticket = Ticket.builder()
//                .id(id)
                .price(price)
                .section(section)
                .build();
        ticket.setId(id);
        return ticket;
    }
}
