package de.fhkiel.advancedjava.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO containing all stations and trafficLines.
 */
@Getter
@Setter
@Builder
public class BackupDTO {

    private List<StationDTO> stops;

    private List<TrafficLineDTO> trafficLines;
}
