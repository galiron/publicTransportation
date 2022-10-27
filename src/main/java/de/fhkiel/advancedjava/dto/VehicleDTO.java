package de.fhkiel.advancedjava.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a Vehicle, consisting of name and assigned Station.
 */
@Setter
@Getter
@Builder
public class VehicleDTO {

    private Long id;

    private String name;

    private StationDTO station;

    @Override
    public String toString() {
        return " {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", " + station +
                '}';
    }
}
