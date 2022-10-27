package de.fhkiel.advancedjava.entities.nodes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Required;

@NodeEntity
@Setter
@Getter
@Builder
public class Ticket extends AbstractNode{

    @Required
    private Double price;

    @Required
    private Section section;
}
