package de.fhkiel.advancedjava.calculation;

import de.fhkiel.advancedjava.dto.ShortestPathSectionDTO;

import java.util.*;

public class Node {

    private Long nodeId;

    private List<Node> shortestPath = new LinkedList<>();

    private Long nodeConnections;

    private ShortestPathSectionDTO sectionUsed;

    public ShortestPathSectionDTO getSectionUsed() {
        return sectionUsed;
    }

    public void setSectionUsed(ShortestPathSectionDTO sectionUsed) {
        this.sectionUsed = sectionUsed;
    }

    public Long getNodeConnections() {
        return nodeConnections;
    }

    public void setNodeConnections(Long nodeConnections) {
        this.nodeConnections = nodeConnections;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    private Integer distance = Integer.MAX_VALUE;

    Map<Node, Integer> adjacentNodes = new HashMap<>();

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public Node(Long nodeId) {
        this.nodeId = nodeId;
    }

}
