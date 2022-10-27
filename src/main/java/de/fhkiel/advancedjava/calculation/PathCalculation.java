package de.fhkiel.advancedjava.calculation;

import de.fhkiel.advancedjava.dto.ShortestPathDTO;
import de.fhkiel.advancedjava.dto.StationDTO;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.entities.nodes.TrafficLine;
import de.fhkiel.advancedjava.enums.StationState;
import de.fhkiel.advancedjava.exceptions.MissingNodeException;
import de.fhkiel.advancedjava.generator.DTOGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PathCalculation {

    public static ShortestPathDTO getShortestPath(Iterable<TrafficLine> allTrafficLinesAsIterable, Station stationA, Station stationB) {
        List<TrafficLine> allTrafficLines = new ArrayList<>();
        allTrafficLinesAsIterable.forEach(allTrafficLines::add);
        List<TrafficLine> allValidTrafficLines = allTrafficLines.stream().filter(
                trafficLine -> trafficLine.getSections().stream().noneMatch(Section::isDisturbed))
                .collect(Collectors.toList());

        allValidTrafficLines.forEach(PathCalculation::markInvalids);
        List<Station> allValidStations = new ArrayList<>();
        allValidTrafficLines.stream().flatMap(trafficLine -> trafficLine.getSections().stream())
                .forEach(section -> {
                    if (section.getStartStation().getStationState().equals(StationState.OPEN) && (!section.getStartStation().isDisturbed()))
                        allValidStations.add(section.getStartStation());

                    if (section.getEndStation().getStationState().equals(StationState.OPEN) && (!section.getEndStation().isDisturbed()))
                        allValidStations.add(section.getEndStation());
                });

        List<Node> allNodes = new ArrayList<>();
        allValidStations.stream().distinct().forEach(station -> allNodes.add(new Node(station.getId())));

        allNodes.stream().distinct().forEach(node -> addDest(allNodes, allTrafficLines, allValidStations, stationB.getId()));
        Graph graph = new Graph();
        allNodes.forEach(graph::addNode);

        graph = Dijkstra.calculateShortestPathFromSource(graph,
                Objects.requireNonNull(allNodes.stream()
                        .filter(nodeItem -> nodeItem.getNodeId().equals(stationA.getId()))
                        .findFirst().orElse(null)));


        List<Node> shortestPath = graph.getNodes().stream().filter(node -> node.getNodeId().equals(stationB.getId())).collect(Collectors.toList());

        List<Station> stations = shortestPath.stream().flatMap(node -> allValidStations.stream().distinct().filter(station -> station.getId().equals(node.getNodeId()))).collect(Collectors.toList());

        ShortestPathDTO shortestPathDTO = ShortestPathDTO.builder().build();
        List<StationDTO> shortestPathRoute = new ArrayList<>();
        shortestPath.forEach(node ->{
                //It's okay to skip the optional isEmtpy check, since we created the nodes from the Stations, they will be contained
                shortestPathRoute.add(
                        DTOGenerator.stationDTOFromStation(allValidStations.stream()
                                .filter(station -> node.getNodeId().equals(station.getId()))
                                .findFirst().get()));
        });

        shortestPathDTO.setTotalTravelTime(shortestPath.stream().mapToInt(Node::getDistance).sum());
        shortestPathDTO.setShortestPath(shortestPath);

        return shortestPathDTO;
    }

    //used to terminate stop time at stations (since they're unavailable)
    private static void markInvalids(TrafficLine trafficLine) {
        trafficLine.getSections().forEach(section -> {
            if (section.getStartStation().isDisturbed() || section.getStartStation().getStationState().equals(StationState.CLOSED))
                section.getStartStation().setConnectionTimeInMin(0);
            if (section.getEndStation().isDisturbed() || section.getEndStation().getStationState().equals(StationState.CLOSED))
                section.getEndStation().setConnectionTimeInMin(0);
        });
    }

    private static boolean nodeHasMultipleLineConnections(List<TrafficLine> trafficLines, Node node) {
        List<List<Station>> stationsListOfTrafficLines = new ArrayList<>();
        trafficLines.forEach(trafficLine -> stationsListOfTrafficLines.add(retrieveDistinctStationsOfTrafficLines(trafficLine)));
        //filter all stations with given nodeId to see how many trafficLines are connected to this node
        long nodeCounter = stationsListOfTrafficLines.stream()
                .filter(stationList -> stationList.stream().anyMatch(station -> station.getId().equals(node.getNodeId()))).count();
        return nodeCounter > 1;
    }

    private static List<Station> retrieveDistinctStationsOfTrafficLines(TrafficLine trafficLine){
        List<Station> startStations = trafficLine.getSections().stream().map(Section::getEndStation).collect(Collectors.toList());
        List<Station> endStations = trafficLine.getSections().stream().map(Section::getStartStation).collect(Collectors.toList());
        List<Station> allStations = new ArrayList<>();
        allStations.addAll(startStations);
        allStations.addAll(endStations);
        return allStations.stream().distinct().collect(Collectors.toList());
    }

    private static void addDest(List<Node> allNodes, List<TrafficLine> trafficLines, List<Station> validStations, Long targetId) {

        allNodes.forEach(nodeToFillWithDestination -> {
            if (!nodeToFillWithDestination.getNodeId().equals(targetId))
                recursionUntilValidStationFound(nodeToFillWithDestination, trafficLines, allNodes, nodeToFillWithDestination.getNodeId(), nodeToFillWithDestination.getNodeId(), new ArrayList<>(), 0);
        });
    }


    private static void recursionUntilValidStationFound(Node nodeToFillWithDestination, List<TrafficLine> trafficLines, List<Node> allNodes, Long latestValidId, Long currentStation, List<Long> ancestors, Integer accumulatedTraveltimeLatestValid) {
        // check all routes (trafficLines)
        trafficLines.forEach(trafficLine -> {
            //if there's only 1 valid station
            //retrieve all sections which start with given node
            List<Section> sectionsWithStartIdOfNode = findAllSectionsWhichStartWithStationId(trafficLines, currentStation);
            // if there's no followup node
            if (sectionsWithStartIdOfNode.isEmpty()) {
                return;
                //throw new IllegalStateException("StartNode should always be present due to checks from previous recursions");
            }
            // if a circle is detected return and proceed with next element
            if (sectionsWithStartIdOfNode.stream().anyMatch(section -> ancestors.contains(section.getEndStation().getId())))
                return;
            sectionsWithStartIdOfNode.forEach(sectionOfStartNode -> {
                // check if this is the last connection, else there are further connections
                Node latestValidNode = retrieveNodeByID(allNodes, latestValidId);
                if (findAllSectionsWhichStartWithStationId(trafficLines, sectionOfStartNode.getEndStation().getId()).size() == 0) {

                    if (latestValidNode == null) // null means invalid node
                        throw new IllegalStateException("There should always be 1 valid Station in a trafficLine!");
                    //if the last station is invalid, get the last known valid station and add a destination
                    if (sectionOfStartNode.getEndStation().isDisturbed() || sectionOfStartNode.getEndStation().getStationState().equals(StationState.CLOSED)) {
                        // catch case where no valid stations are found at all
                        if (latestValidNode.equals(nodeToFillWithDestination)) return;
                        latestValidNode.addDestination(latestValidNode
                                , accumulatedTraveltimeLatestValid);
                    }
                    // is valid endStation
                    else {
                        Node finalStation = retrieveNodeByID(allNodes, sectionOfStartNode.getEndStation().getId());
                        if (finalStation == null)
                            throw new IllegalStateException("If you ever get here, you've messed something up really, really bad.");
                        latestValidNode.addDestination(finalStation
                                //travel time is based on previous recursions
                                , accumulatedTraveltimeLatestValid + sectionOfStartNode.getDurationInMinutes());
                        //endStation is reached
                        return;
                    }
                }
                // no valid finalEndStation node found but further nodes exist
                else {
                    ancestors.add(sectionOfStartNode.getEndStation().getId());
                    recursionUntilValidStationFound(nodeToFillWithDestination, trafficLines, allNodes, latestValidId, sectionOfStartNode.getEndStation().getId(), ancestors, accumulatedTraveltimeLatestValid + sectionOfStartNode.getDurationInMinutes());
                }
                //check if the following node(endStation of each section) has multiple connections
                Node destinationNode = retrieveNodeByID(allNodes, sectionOfStartNode.getEndStation().getId());

                // when the following node is valid...
                if(destinationNode != null) {
                    // ...and has multiple connections
                    if (nodeHasMultipleLineConnections(trafficLines, destinationNode)) {
                        List<Long> endStationsIdsOfStartNode = sectionsWithStartIdOfNode.stream().map(section -> section.getEndStation().getId()).collect(Collectors.toList());
                        // add current endStation as ancestor for the next recursion to prevent circles
                        ancestors.add(sectionOfStartNode.getEndStation().getId());
                        latestValidNode.addDestination(destinationNode,
                                accumulatedTraveltimeLatestValid
                                        + sectionOfStartNode.getEndStation().getConnectionTimeInMin()
                                        + sectionOfStartNode.getDurationInMinutes());
                        recursionUntilValidStationFound(nodeToFillWithDestination, trafficLines, allNodes, sectionOfStartNode.getEndStation().getId(), sectionOfStartNode.getEndStation().getId(), ancestors, 0);
                    }
                    // ... and has no multiple connections, add the endStation as node
                    else {
                        latestValidNode.addDestination(destinationNode
                                , accumulatedTraveltimeLatestValid + sectionOfStartNode.getDurationInMinutes());
                    }
                }
            });
        });
    }

    private static Node retrieveNodeByID(List<Node> allNodes, Long id) {
        Optional<Node> destinationNode = allNodes.stream().filter(node -> node.getNodeId().equals(id)).findFirst();
        if (destinationNode.isEmpty())
            return null;
        return destinationNode.get();
    }

    private static List<Section> findAllSectionsWhichStartWithStationId(List<TrafficLine> allTrafficLines, Long idOfStation) {
        return allTrafficLines.stream()
                .flatMap(trafficLine -> trafficLine.getSections().stream())
                .filter(section ->
                        section.getStartStation().getId().equals(idOfStation)).collect(Collectors.toList());

    }

}

