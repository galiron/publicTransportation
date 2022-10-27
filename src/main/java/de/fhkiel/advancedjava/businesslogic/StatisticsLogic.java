package de.fhkiel.advancedjava.businesslogic;

import de.fhkiel.advancedjava.dto.StatisticsDTO;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.enums.MeanOfTransport;
import de.fhkiel.advancedjava.generator.DTOGenerator;
import de.fhkiel.advancedjava.repository.SectionRepository;
import de.fhkiel.advancedjava.repository.StationRepository;
import de.fhkiel.advancedjava.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.fhkiel.advancedjava.enums.MeanOfTransport.*;

@Service
public class StatisticsLogic {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final TicketRepository ticketRepository;


    @Autowired
    public StatisticsLogic(SectionRepository sectionRepository, StationRepository stationRepository, TicketRepository ticketRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.ticketRepository = ticketRepository;

    }

    /**
     * Generates a statistic from the repository as plaintext.
     *
     * @return String of statistic
     */
    public String getStatistics() {
        List<Section> listOfAllSections = new ArrayList<>();
        List<Station> listOfAllStations = new ArrayList<>();
        HashMap<Long, Integer> ticketsPerConnection = new HashMap<>();
        HashMap<Long, Integer> disturbancesOnSections = new HashMap<>(); // containing the id and disturbances that occurred
        HashMap<Long, Integer> disturbancesOnStops = new HashMap<>();
        Iterable<Section> iterableOfAllSections = sectionRepository.findAll();
        iterableOfAllSections.forEach(listOfAllSections::add);
        Iterable<Station> iterableOfAllStations = stationRepository.findAll();
        iterableOfAllStations.forEach(listOfAllStations::add);
        StatisticsDTO statistic = DTOGenerator.defaultStatisticsDTO();

        listOfAllSections.stream().distinct().forEach(section ->
                ticketsPerConnection.put(
                        section.getId()
                        , ticketRepository.findAllBySectionId(section.getId()).size())
        );

        listOfAllSections.stream().distinct().forEach(section ->
                disturbancesOnSections.put(
                        section.getId()
                        , section.getDisturbanceCounter()
                ));

        listOfAllStations.stream().distinct().forEach(station ->
                disturbancesOnStops.put(
                        station.getStopId()
                        , station.getDisturbanceCounter()
                ));


        //assertion
        statistic.setTotalNumberOfTicketsBought(ticketRepository.findAll().size());

        statistic.setMapOfTotalNumberOfTicketsBoughtPerConnection(ticketsPerConnection);

        int disturbanceOnSectionCount = 0;
        int disturbanceOnStopsCount = 0;
        for(Map.Entry<Long,Integer> entry: disturbancesOnSections.entrySet()) {
            disturbanceOnSectionCount += entry.getValue();
        }
        for(Map.Entry<Long,Integer> entry: disturbancesOnStops.entrySet()) {
            disturbanceOnStopsCount += entry.getValue();
        }
        statistic.setTotalNumberOfDisturbances(
                (disturbanceOnSectionCount + disturbanceOnStopsCount)
        );

        statistic.setMapOfDisturbancesOnConnections(disturbancesOnSections);

        statistic.setMapOfDisturbancesOnStops(disturbancesOnSections);

        statistic.setTotalNumberOfStops(listOfAllStations.size());

        statistic.setTotalNumberOfBusStops(
                getNumberOfStopsOfVehicleType(listOfAllStations, BUS)
        );

        statistic.setTotalNumberOfSubwayStops(
                getNumberOfStopsOfVehicleType(listOfAllStations, SUBWAY)
        );

        statistic.setTotalNumberOfSuburbanTrainStops(
                getNumberOfStopsOfVehicleType(listOfAllStations, SUBURBAN_TRAIN)
        );

        statistic.setTotalNumberOfConnections(listOfAllSections.size());

        return statistic.toString();
    }

    private int getNumberOfStopsOfVehicleType(List<Station> listOfAllStations, MeanOfTransport vehicleType){
        return (int) listOfAllStations.stream()
                .distinct()
                .filter(station -> station.getMeansOfTransport().contains(vehicleType)).count();

    }
}
