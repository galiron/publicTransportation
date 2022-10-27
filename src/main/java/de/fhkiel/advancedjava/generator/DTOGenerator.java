package de.fhkiel.advancedjava.generator;

import de.fhkiel.advancedjava.dto.*;
import de.fhkiel.advancedjava.entities.nodes.*;
import de.fhkiel.advancedjava.enums.DisturbanceType;
import de.fhkiel.advancedjava.enums.MeanOfTransport;
import de.fhkiel.advancedjava.enums.StationState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * DTO generator class
 *
 * @author Erik Bannasch
 */
public class DTOGenerator {


    /**
     * Creates a StationDTO from station
     *
     * @param station station to convert
     * @return StationDTO
     */
    public static StationDTO stationDTOFromStation(Station station) {
        return customStationDTO(station.getId(), station.getStopId(), station.getName(), station.getCity(), station.getMeansOfTransport(), station.getStationState(), station.getConnectionTimeInMin(), station.isDisturbed(), station.getDisturbanceCounter());
    }

    /**
     * Creates a station with custom parameters
     *
     * @param id                  id
     * @param stopId              stopId
     * @param name                name
     * @param city                city
     * @param transportationType  transportationType
     * @param state               state
     * @param connectionTimeInMin connectionTimeInMin
     * @param disturbed           disturbed
     * @param disturbanceCounter  disturbanceCounter
     * @return StationDTO
     */
    public static StationDTO customStationDTO(Long id, Long stopId, String name, String city, List<MeanOfTransport> transportationType, StationState state, Integer connectionTimeInMin, Boolean disturbed, Integer disturbanceCounter) {

        return StationDTO.builder()
                .id(id)
                .stopId(stopId)
                .name(name)
                .city(city)
                .state(state)
                .types(transportationType)
                .disturbed(disturbed)
                .connectionTimeInMin(connectionTimeInMin)
                .disturbanceCounter(disturbanceCounter)
                .build();
    }

    /**
     * Creates a SectionDTO from section
     *
     * @param section Section to convert
     * @return SectionDTO
     */
    public static SectionDTO sectionDTOFromSection(Section section) {
        return customSectionDTO(section.getId()
                , section.getStartStation().getStopId()
                , section.getEndStation().getStopId()
                , section.getDurationInMinutes()
                , section.isDisturbed()
                , section.getDisturbanceCounter());
    }

    /**
     * Creates a SectionDTO with custom parameters
     *
     * @param id                 id
     * @param beginStopId        beginStopId
     * @param endStopId          endStopId
     * @param durationInMinutes  durationInMinutes
     * @param disturbed          disturbed
     * @param disturbanceCounter disturbanceCounter
     * @return SectionDTO
     */
    public static SectionDTO customSectionDTO(Long id, Long beginStopId, Long endStopId, Integer durationInMinutes, Boolean disturbed, Integer disturbanceCounter) {
        return SectionDTO.builder()
                .sectionId(id)
                .beginStopId(beginStopId)
                .endStopId(endStopId)
                .durationInMinutes(durationInMinutes)
                .disturbed(disturbed)
                .disturbanceCounter(disturbanceCounter)
                .build();
    }

    /**
     * Creates a TrafficLineDTO with custom parameters
     *
     * @param id              id
     * @param lineId          lineId
     * @param name            name
     * @param meanOfTransport meanOfTransport
     * @param sections        sections
     * @return TrafficLineDTO
     */
    public static TrafficLineDTO customTrafficLineDTO(Long id, Long lineId, String name, MeanOfTransport meanOfTransport, List<SectionDTO> sections) {
        return TrafficLineDTO.builder()
                .id(id)
                .lineId(lineId)
                .name(name)
                .type(meanOfTransport)
                .sections(sections)
                .build();
    }

    /**
     * Creates a TrafficLineDTO from TrafficLine
     *
     * @param trafficLine TrafficLine to convert
     * @return TrafficLineDTO
     */
    public static TrafficLineDTO trafficLineDTOFromTrafficLine(TrafficLine trafficLine) {
        List<SectionDTO> sections = new ArrayList<>();
        trafficLine.getSections().forEach(section -> sections.add(DTOGenerator.sectionDTOFromSection(section)));
        return customTrafficLineDTO(
                trafficLine.getId()
                , trafficLine.getLineId()
                , trafficLine.getName()
                , trafficLine.getMeanOfTransport()
                , sections
        );
    }

    /**
     * Creates a VehicleDTO with custom parameters
     *
     * @param id      id
     * @param name    name
     * @param station station
     * @return VehicleDTO
     */
    public static VehicleDTO customVehicleDTO(Long id, String name, StationDTO station) {
        return VehicleDTO.builder()
                .id(id)
                .name(name)
                .station(station)
                .build();
    }

    /**
     * Creates a VehicleDTO from Vehicle
     *
     * @param vehicle Vehicle to convert
     * @return VehicleDTO
     */
    public static VehicleDTO vehicleDTOFromVehicle(Vehicle vehicle) {
        if (vehicle.getStation() == null){
            return customVehicleDTO(vehicle.getId()
                    , vehicle.getName()
                    , null);
        }
        return customVehicleDTO(vehicle.getId()
                , vehicle.getName()
                , stationDTOFromStation(vehicle.getStation()));
    }

    /**
     * Creates a VehicleAtStationDTO with custom parameters
     *
     * @param id      id
     * @param station station
     * @return VehicleAtStationDTO
     */
    public static VehicleAtStationDTO customVehicleAtStationDTO(Long id, StationDTO station) {
        return VehicleAtStationDTO.builder()
                .id(id)
                .station(station)
                .build();
    }

    /**
     * Creates a TicketDTO with custom parameters
     *
     * @param id      id
     * @param price   price
     * @param section section
     * @return TicketDTO
     */
    public static TicketDTO customTicketDTO(Long id, Double price, SectionDTO section) {
        return TicketDTO.builder()
                .id(id)
                .price(price)
                .section(section)
                .build();
    }

    /**
     * Creates a TicketDTO from Ticket
     *
     * @param ticket Ticket to convert
     * @return TicketDTO
     */
    public static TicketDTO ticketDTOFromTicket(Ticket ticket) {
        return customTicketDTO(
                ticket.getId()
                , ticket.getPrice()
                , DTOGenerator.sectionDTOFromSection(ticket.getSection()));
    }

    /**
     * Creates an empty (default) StatisticsDTO
     *
     * @return StatisticsDTO
     */
    public static StatisticsDTO defaultStatisticsDTO() {
        return StatisticsDTO.builder().build();
    }

    /**
     * Creates a StatisticsDTO with custom Parameters
     *
     * @param totalNumberOfTicketsBought                   Represents the total number of Tickets bought
     * @param mapOfTotalNumberOfTicketsBoughtPerConnection Contains a map of Section Id's and the amount of tickets bough for the Section
     * @param totalNumberOfDisturbances                    Contains the number of Disturbances occurred on Stations and Sections
     * @param mapOfDisturbancesOnConnections               Contains a map of Section Id's and the amount of disturbances occurred on this Section
     * @param mapOfDisturbancesOnStops                     Contains a map of Station Id's and the amount of disturbances occurred on this Station
     * @param totalNumberOfStops                           Contains the total number of Stations
     * @param totalNumberOfBusStops                        Contains the total number of Stations of type BUS
     * @param totalNumberOfSubwayStops                     Contains the total number of Stations of type SUBWAY
     * @param totalNumberOfSuburbanTrainStops              Contains the total number of Stations of type SUBURBAN_TRAIN
     * @param totalNumberOfConnections                     Contains the total number of Sections
     * @return StatisticsDTO
     */
    public static StatisticsDTO customStatisticsDTO(Integer totalNumberOfTicketsBought, HashMap<Long, Integer> mapOfTotalNumberOfTicketsBoughtPerConnection, Integer totalNumberOfDisturbances, HashMap<Long, Integer> mapOfDisturbancesOnConnections, HashMap<Long, Integer> mapOfDisturbancesOnStops, Integer totalNumberOfStops, Integer totalNumberOfBusStops, Integer totalNumberOfSubwayStops, Integer totalNumberOfSuburbanTrainStops, Integer totalNumberOfConnections) {
        return StatisticsDTO.builder()
                .totalNumberOfTicketsBought(totalNumberOfTicketsBought)
                .mapOfTotalNumberOfTicketsBoughtPerConnection(mapOfTotalNumberOfTicketsBoughtPerConnection)
                .totalNumberOfDisturbances(totalNumberOfDisturbances)
                .mapOfDisturbancesOnConnections(mapOfDisturbancesOnConnections)
                .mapOfDisturbancesOnStops(mapOfDisturbancesOnStops)
                .totalNumberOfStops(totalNumberOfStops)
                .totalNumberOfBusStops(totalNumberOfBusStops)
                .totalNumberOfSubwayStops(totalNumberOfSubwayStops)
                .totalNumberOfSuburbanTrainStops(totalNumberOfSuburbanTrainStops)
                .totalNumberOfConnections(totalNumberOfConnections)
                .build();
    }

    /**
     * Creates a BackupDTO from a customized List of StationDTO's and TrafficLineDTO's
     *
     * @param stops        stops the BackupDTO is based on
     * @param trafficLines trafficLines the BackupDTO is based on
     * @return BackupDTO
     */
    public static BackupDTO customBackupDTO(List<StationDTO> stops, List<TrafficLineDTO> trafficLines) {
        return BackupDTO.builder()
                .stops(stops)
                .trafficLines(trafficLines)
                .build();

    }

    /**
     * Creates a BackupDTO from a List of Stations and TrafficLines
     *
     * @param stations     Station List the BackupDTO stops are based on
     * @param trafficLines TrafficLine List the BackupDTO trafficLines are based on
     * @return BackupDTO
     */
    public static BackupDTO backupDTOFromStationsAndTrafficLines(List<Station> stations, List<TrafficLine> trafficLines) {
        List<StationDTO> stationDTOS = new ArrayList<>();
        stations.forEach(station -> stationDTOS.add(stationDTOFromStation(station)));
        List<TrafficLineDTO> trafficLineDTOS = new ArrayList<>();
        trafficLines.forEach(trafficLine -> trafficLineDTOS.add(trafficLineDTOFromTrafficLine(trafficLine)));
        return customBackupDTO(stationDTOS, trafficLineDTOS);
    }

    /**
     * Creates a Tuple of two StationDTO's
     *
     * @param startStation Element A of StationTupleDTO
     * @param endStation   Element B of StationTupleDTO
     * @return StationTupleDTO
     */
    public static StationTupleDTO customStationDupleDTO(StationDTO startStation, StationDTO endStation) {
        return StationTupleDTO.builder()
                .stationA(startStation)
                .stationB(endStation)
                .build();
    }

    /**
     * Creates a DisturbanceDTO with custom parameters.
     *
     * @param id              id of element to disturb.
     * @param disturbanceType type of element to disturb.
     * @return DisturbanceDTO
     */
    public static DisturbanceDTO customDisturbanceDTO(Long id, DisturbanceType disturbanceType) {
        return DisturbanceDTO.builder()
                .idOfDisturbedElement(id)
                .disturbanceType(disturbanceType)
                .build();
    }

    public static OnlyStationStateDTO customOnlyStationStateDTO(Long stopId, StationState stationState){
        return OnlyStationStateDTO.builder()
                .stopId(stopId)
                .stationState(stationState)
                .build();
    }
}
