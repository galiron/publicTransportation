package de.fhkiel.advancedjava.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a Vehicle (id of Vehicle) at a Station.
 */
@Getter
@Setter
@Builder
public class VehicleAtStationDTO {

    private Long id;

    private StationDTO station;

}
