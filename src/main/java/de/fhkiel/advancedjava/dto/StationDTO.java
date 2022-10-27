package de.fhkiel.advancedjava.dto;

import de.fhkiel.advancedjava.enums.MeanOfTransport;
import de.fhkiel.advancedjava.enums.StationState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO representing a Station.
 */
@Setter
@Getter
@Builder
public class StationDTO implements Comparable<StationDTO> {

    private Long id;

    private Long stopId;

    private String name;

    private String city;

    @Builder.Default
    private StationState state = StationState.CLOSED;

    private List<MeanOfTransport> types;

    @Builder.Default
    private Integer connectionTimeInMin = 0;

    @Builder.Default
    private Boolean disturbed = false;

    @Builder.Default
    private Integer disturbanceCounter = 0;

    @Override
    public int compareTo(StationDTO o) {
        return this.getStopId().compareTo(o.getStopId());
    }

    @Override
    public String toString() {
        return "stopId=" + stopId;
    }
}
