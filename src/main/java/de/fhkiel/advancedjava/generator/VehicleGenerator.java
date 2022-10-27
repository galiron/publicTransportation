package de.fhkiel.advancedjava.generator;

import de.fhkiel.advancedjava.dto.VehicleDTO;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.entities.nodes.Vehicle;

public class VehicleGenerator {

    /**
     * Creates Vehicle from custom parameters
     * @param id will be ignored, currently stopId is used for Identification
     * @param name name
     * @param station station
     * @return Vehicle
     */
    public static Vehicle customVehicle(Long id, String name, Station station){
        Vehicle vehicle = Vehicle.builder()
//                .id(id)
                .name(name)
                .station(station)
                .build();
        vehicle.setId(id);
        return vehicle;
    }

    /**
     * Creates a default Vehicle (no id)
     * @param name name
     * @param station station
     * @return Vehicle
     */
    public static Vehicle defaultVehicle(String name, Station station){
        return customVehicle(null, name, station);
    }

    /**
     * Creates Vehicle from VehicleDTO
     * @param vehicleDTO vehicleDTO to convert
     * @return Vehicle
     */
    public static Vehicle createVehicleFromDTO(VehicleDTO vehicleDTO){
        if(vehicleDTO.getStation() == null) return customVehicle(vehicleDTO.getId(),vehicleDTO.getName(), null);

        return customVehicle(vehicleDTO.getId(),vehicleDTO.getName(),StationGenerator.createStationFromDto(vehicleDTO.getStation()));
    }
}
