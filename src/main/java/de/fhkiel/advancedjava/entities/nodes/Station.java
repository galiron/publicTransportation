package de.fhkiel.advancedjava.entities.nodes;


import com.fasterxml.jackson.annotation.JsonProperty;
import de.fhkiel.advancedjava.enums.MeanOfTransport;
import de.fhkiel.advancedjava.enums.StationState;
import de.fhkiel.advancedjava.entities.nodes.shared.Disturbable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Required;

import java.util.List;

@NodeEntity
@Getter
@Setter
@Builder
public class Station extends AbstractNode implements Disturbable, Comparable<Station> {

    @Required
    private Long stopId;

    @Required
    private String name;

    @Required
    private String city;

    @Builder.Default
    private StationState stationState = StationState.CLOSED;

    @Required
    @JsonProperty("types")
    private List<MeanOfTransport> meansOfTransport;

    @Builder.Default
    private Integer connectionTimeInMin = 0;

    @Builder.Default
    private Boolean disturbed = false;

    @Builder.Default
    private Integer disturbanceCounter = 0;

    public Boolean isDisturbed() {
        return disturbed;
    }

    public void setDisturbed(Boolean disturbed) {
        this.disturbed = disturbed;
        if(disturbed) ++disturbanceCounter;
    }

    @Override
    public int compareTo(Station o) {
        return this.getStopId().compareTo(o.getStopId());
    }


}
