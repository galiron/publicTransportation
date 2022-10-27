package de.fhkiel.advancedjava.entities.nodes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Required;

@NodeEntity
@Getter
@Setter
@Builder
public class Vehicle extends AbstractNode {

    @Required
    private String name;

    @Relationship(type = "StoppingAt", direction = Relationship.INCOMING)
    private Station station;

}
