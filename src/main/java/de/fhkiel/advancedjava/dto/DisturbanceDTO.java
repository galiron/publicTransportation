package de.fhkiel.advancedjava.dto;

import de.fhkiel.advancedjava.enums.DisturbanceType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO containing the id and type of the affected element.
 */
@Setter
@Getter
@Builder
public class DisturbanceDTO {

    private Long idOfDisturbedElement;

    private DisturbanceType disturbanceType;
}
