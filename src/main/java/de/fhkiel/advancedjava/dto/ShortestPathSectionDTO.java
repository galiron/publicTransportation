package de.fhkiel.advancedjava.dto;


import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.generator.DTOGenerator;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO containing the start-, endStation, sectionTravel- and connectionTime.
 */
@Setter
@Getter
public class ShortestPathSectionDTO {

    private StationDTO startStation;

    private StationDTO endStation;

    private int sectionTravelTime;

    private int connectionTime;

    // builder constructor was waived because of low complexity and use cases (only 2)
    public ShortestPathSectionDTO(Station startStation, Station endStation, int sectionTravelTime, int connectionTime) {
        this.startStation = DTOGenerator.stationDTOFromStation(startStation);
        this.endStation = DTOGenerator.stationDTOFromStation(endStation);
        this.sectionTravelTime = sectionTravelTime;
        this.connectionTime = connectionTime;
    }
}
