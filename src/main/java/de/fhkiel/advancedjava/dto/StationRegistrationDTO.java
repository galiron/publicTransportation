package de.fhkiel.advancedjava.dto;



import de.fhkiel.advancedjava.enums.MeanOfTransport;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO consisting of the base data (name, city and transportationType) to create a station.
 */
@Getter
@Setter
public class StationRegistrationDTO {

    private String name;

    private String city;

    private List<MeanOfTransport> transportationType;

}
