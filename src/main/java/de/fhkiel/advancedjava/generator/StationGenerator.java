package de.fhkiel.advancedjava.generator;

import de.fhkiel.advancedjava.dto.StationDTO;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.enums.MeanOfTransport;
import de.fhkiel.advancedjava.enums.StationState;

import java.util.List;


public class StationGenerator {

    /**
     * Creates a Station from custom parameters
     *
     * @param id                  will be ignored, currently stopId is used for Identification
     * @param stopId              stopId
     * @param name                name
     * @param city                city
     * @param transportationType  transportationType
     * @param state               state
     * @param connectionTimeInMin connectionTimeInMin
     * @param disturbed           disturbed
     * @param disturbanceCounter  disturbanceCounter
     * @return Station
     */
    public static Station customStation(Long id, Long stopId, String name, String city, List<MeanOfTransport> transportationType, StationState state, Integer connectionTimeInMin, Boolean disturbed, Integer disturbanceCounter) {
        Station station = Station.builder()
//                .id(id)
                .stopId(stopId)
                .name(name)
                .city(city)
                .stationState(state)
                .meansOfTransport(transportationType)
                .disturbed(disturbed)
                .connectionTimeInMin(connectionTimeInMin)
                .disturbanceCounter(disturbanceCounter)
                .build();
        station.setId(id);
        return station;
    }

    /**
     * Creates a Station from StationDTO
     *
     * @param stationDTO StationDTO to convert
     * @return Station
     */
    public static Station createStationFromDto(StationDTO stationDTO) {
        if (stationDTO.getConnectionTimeInMin() == null) stationDTO.setConnectionTimeInMin(0);
        if (stationDTO.getDisturbed() == null) stationDTO.setDisturbed(false);
        if (stationDTO.getDisturbanceCounter() == null) stationDTO.setDisturbanceCounter(0);
        return customStation(stationDTO.getId(), stationDTO.getStopId(), stationDTO.getName(), stationDTO.getCity(), stationDTO.getTypes(), stationDTO.getState(), stationDTO.getConnectionTimeInMin(), stationDTO.getDisturbed(), stationDTO.getDisturbanceCounter());
    }

    /**
     * Creates a default Station from StationDTO (no connection time, disturbance and a CLOSED state)
     *
     * @param stationDTO StationDTO to convert
     * @return Station
     */
    public static Station createDefaultStationFromDTO(StationDTO stationDTO) {
        return customStation(stationDTO.getId(), stationDTO.getStopId(), stationDTO.getName(), stationDTO.getCity(), stationDTO.getTypes(), StationState.CLOSED, 0, false, 0);
    }

    /**
     * Creates a default Station (no connection time, disturbance and a CLOSED state)
     *
     * @param stopId             stopId
     * @param name               name
     * @param city               city
     * @param transportationType transportationType
     * @return Station will be ignored, currently stopId is used for Identification
     */
    public static Station defaultStation(Long stopId, String name, String city, List<MeanOfTransport> transportationType) {
        return customStation(null, stopId, name, city, transportationType, StationState.OPEN, 0, false, 0);
    }
}
