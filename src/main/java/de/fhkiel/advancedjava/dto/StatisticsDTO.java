package de.fhkiel.advancedjava.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * DTO containing all important/necessary statistic fields.
 */
@Getter
@Setter
@Builder
public class StatisticsDTO {

    private int totalNumberOfTicketsBought;

    private HashMap<Long,Integer> mapOfTotalNumberOfTicketsBoughtPerConnection;

    private int totalNumberOfDisturbances;

    private HashMap<Long,Integer> mapOfDisturbancesOnConnections;

    private HashMap<Long,Integer> mapOfDisturbancesOnStops;

    private int totalNumberOfStops;

    private int totalNumberOfBusStops;

    private int totalNumberOfSubwayStops;

    private int totalNumberOfSuburbanTrainStops;

    private int totalNumberOfConnections;

    @Override
    public String toString() {
        //.values().stream().mapToInt(value -> value).sum()
        return "StatisticsDTO{" +
                "totalNumberOfTicketsBought=" + totalNumberOfTicketsBought +
                ", mapOfTotalNumberOfTicketsBoughtPerConnection=" + mapOfTotalNumberOfTicketsBoughtPerConnection +
                ", totalNumberOfDisturbances=" + totalNumberOfDisturbances +
                ", mapOfDisturbancesOnConnections=" + mapOfDisturbancesOnConnections +
                ", mapOfDisturbancesOnStops=" + mapOfDisturbancesOnStops +
                ", totalNumberOfStops=" + totalNumberOfStops +
                ", totalNumberOfBusStops=" + totalNumberOfBusStops +
                ", totalNumberOfSubwayStops=" + totalNumberOfSubwayStops +
                ", totalNumberOfSuburbanTrainStops=" + totalNumberOfSuburbanTrainStops +
                ", totalNumberOfConnections=" + totalNumberOfConnections +
                '}';
    }
}
