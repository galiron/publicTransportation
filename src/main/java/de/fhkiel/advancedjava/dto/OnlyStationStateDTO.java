package de.fhkiel.advancedjava.dto;

import de.fhkiel.advancedjava.enums.StationState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO consisting of id and state of a Station
 */
@Setter
@Getter
@Builder
public class OnlyStationStateDTO {

    private Long stopId;

    private StationState stationState;
}
