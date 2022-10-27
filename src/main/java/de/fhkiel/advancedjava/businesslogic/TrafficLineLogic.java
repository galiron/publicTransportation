package de.fhkiel.advancedjava.businesslogic;


import de.fhkiel.advancedjava.calculation.PathCalculation;
import de.fhkiel.advancedjava.dto.SectionDTO;
import de.fhkiel.advancedjava.dto.ShortestPathDTO;
import de.fhkiel.advancedjava.dto.TrafficLineDTO;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.entities.nodes.TrafficLine;
import de.fhkiel.advancedjava.exceptions.InsufficientNodeException;
import de.fhkiel.advancedjava.exceptions.MissingNodeException;
import de.fhkiel.advancedjava.exceptions.NotSupportedTransportationTypeException;
import de.fhkiel.advancedjava.generator.DTOGenerator;
import de.fhkiel.advancedjava.generator.SectionGenerator;
import de.fhkiel.advancedjava.generator.TrafficLineGenerator;
import de.fhkiel.advancedjava.repository.SectionRepository;
import de.fhkiel.advancedjava.repository.StationRepository;
import de.fhkiel.advancedjava.repository.TrafficLineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class TrafficLineLogic {

    private final TrafficLineRepository trafficLineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    /**
     * Creates a TrafficLine in the repository.
     *
     * @param trafficLineDto TrafficLine that shall be created.
     * @return Created TrafficLine.
     * @throws NotSupportedTransportationTypeException Gets thrown if a Station type is not compliant with the TrafficLine type.
     * @throws MissingNodeException                    Gets thrown if a Node couldn't be found in the repository.
     * @throws InsufficientNodeException               Gets thrown if a Node dependency is missing.
     */
    public TrafficLineDTO createTrafficLine(TrafficLineDTO trafficLineDto) throws NotSupportedTransportationTypeException, MissingNodeException, InsufficientNodeException {
        List<Section> generatedSections = new ArrayList<>(); //needed to delete sections if creation of trafficLine fails
        List<Section> sectionsFromDB = new ArrayList<>(); //needed to delete sections if creation of trafficLine fails

        for (SectionDTO sectionDTO : trafficLineDto.getSections()) {
            SortedSet<Station> startStation = stationRepository.findByStopId(sectionDTO.getBeginStopId());
            SortedSet<Station> endStation = stationRepository.findByStopId(sectionDTO.getEndStopId());
            if (startStation.isEmpty() || endStation.isEmpty()) {
                throw new MissingNodeException("Could not retrieve stations from DB! { startStationId: " + startStation + " | endStationId" + endStation + " }");
            }
            Optional<Section> sectionFromDb = sectionRepository.findByStartStation_IdAndEndStation_IdAndDurationInMinutes(startStation.first().getId(), endStation.first().getId(), sectionDTO.getDurationInMinutes());
            if (sectionFromDb.isEmpty()) {
                generatedSections.add(SectionGenerator.customSection(null, startStation.first(), endStation.first(), sectionDTO.getDurationInMinutes(), sectionDTO.getDisturbed(), sectionDTO.getDisturbanceCounter()));
            } else {
                sectionsFromDB.add(sectionFromDb.get());
            }
        }

        List<Section> totalSections = new ArrayList<>(generatedSections);
        totalSections.addAll(sectionsFromDB);
        if (totalSections.size() <= 1) {
            throw new InsufficientNodeException("TrafficLine can not be created with just a single Section!");
        }
        try {
            totalSections.forEach(section -> {
                if (!section.getStartStation().getMeansOfTransport().contains(trafficLineDto.getType()))
                    throw new IllegalArgumentException();
                if (!section.getEndStation().getMeansOfTransport().contains(trafficLineDto.getType()))
                    throw new IllegalArgumentException();
            });
        } catch (IllegalArgumentException e) {
            throw new NotSupportedTransportationTypeException("A chosen stop does not support the type of traffic line!");
        }

        TrafficLine savedTrafficLine;
        if (totalSections.stream().noneMatch(section -> section.getStartStation().equals(section.getEndStation()))) {
            savedTrafficLine = trafficLineRepository.save(
                    TrafficLineGenerator.createTrafficLineFromDTO(trafficLineDto, totalSections));
        } else {
            throw new IllegalStateException("The delivered list contains sections with equal start and ending stations.");
        }


        return DTOGenerator.trafficLineDTOFromTrafficLine(savedTrafficLine);
    }

    /**
     * Calculates the fastest connection from station A->B
     *
     * @param stationA StartStation
     * @param stationB EndStation
     * @return DTO Containing the information about the shortest Path.
     */
    public ShortestPathDTO getFastestConnection(Station stationA, Station stationB) {
        try {
            Iterable<TrafficLine> allTrafficLinesAsIterable = trafficLineRepository.findAll(2);
            return PathCalculation.getShortestPath(allTrafficLinesAsIterable, stationA, stationB);
        } catch (NoSuchElementException e) {
            //TODO: error handling : no line available
        }
        throw new IllegalStateException("reached impossible state within the traffic line logic");
    }
}
