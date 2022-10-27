package de.fhkiel.advancedjava.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO consisting of id, price and section of a Ticket.
 */
@Setter
@Getter
@Builder
public class TicketDTO {

    private Long id;

    private Double price;

    private SectionDTO section;
}
