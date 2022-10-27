package de.fhkiel.advancedjava.entities.nodes;


import de.fhkiel.advancedjava.entities.nodes.shared.Disturbable;
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
public class Section extends AbstractNode implements Disturbable, Comparable<Section> {

    @Required
    @Relationship(type = "START_STATION", direction = Relationship.INCOMING)
    private Station startStation;

    @Required
    @Relationship(type = "END_STATION", direction = Relationship.INCOMING)
    private Station endStation;

    @Required
    @Builder.Default
    private Integer durationInMinutes = 0;

    @Builder.Default
    @Required
    private Boolean disturbed = false;

    @Builder.Default
    @Required
    private Integer disturbanceCounter = 0;

    public Boolean isDisturbed() {
        return disturbed;
    }

    public void setDisturbed(Boolean disturbed) {
        this.disturbed = disturbed;
        if (disturbed) ++disturbanceCounter;

    }

    @Override
    public int compareTo(Section o) {

        return this.getStartStation().getStopId().compareTo(o.getStartStation().getStopId());
    }
}
