package de.fhkiel.advancedjava.entities.nodes;


import com.fasterxml.jackson.annotation.JsonProperty;
import de.fhkiel.advancedjava.enums.MeanOfTransport;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Required;

import java.util.List;

@NodeEntity
@Setter
@Getter
@Builder
public class TrafficLine extends AbstractNode implements Comparable<TrafficLine> {

    @Required
    private Long lineId;

    @Required
    private String name;

    @Required
    @JsonProperty("type")
    private MeanOfTransport meanOfTransport;

    @Required
    @Relationship(type = "Section", direction = Relationship.INCOMING)
    private List<Section> sections;

    @Override
    public int compareTo(TrafficLine o) {
        return this.getId().compareTo(o.getId());
    }

}
