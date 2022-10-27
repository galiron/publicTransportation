package de.fhkiel.advancedjava.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO to represent a tuple of StationDTOs.
 */
@Setter
@Getter
@Builder
public class StationTupleDTO {

    private StationDTO stationA;

    private StationDTO stationB;
}
