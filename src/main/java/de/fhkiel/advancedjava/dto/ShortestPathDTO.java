package de.fhkiel.advancedjava.dto;


import de.fhkiel.advancedjava.calculation.Node;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO consisting of shortest path and travel time.
 */
@Setter
@Getter
@Builder
public class ShortestPathDTO {

    private List<Node> shortestPath;

    private Integer totalTravelTime;

    // builder constructor was waived because of low complexity and usage
    public ShortestPathDTO(List<Node> shortestPath, Integer totalTravelTime) {
        this.shortestPath = shortestPath;
        this.totalTravelTime = totalTravelTime;
    }
}
