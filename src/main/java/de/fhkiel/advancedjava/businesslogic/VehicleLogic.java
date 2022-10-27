package de.fhkiel.advancedjava.businesslogic;


import de.fhkiel.advancedjava.dto.VehicleAtStationDTO;
import de.fhkiel.advancedjava.dto.VehicleDTO;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.entities.nodes.Vehicle;
import de.fhkiel.advancedjava.exceptions.MissingNodeException;
import de.fhkiel.advancedjava.generator.DTOGenerator;
import de.fhkiel.advancedjava.generator.VehicleGenerator;
import de.fhkiel.advancedjava.repository.StationRepository;
import de.fhkiel.advancedjava.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class VehicleLogic {

    private final VehicleRepository vehicleRepository;
    private final StationRepository stationRepository;

    //TODO: implement logic to create Vehicle

    /**
     * Assigns a Vehicle to a Station.
     *
     * @param vehicleAtStationDTO Containing Vehicle ID and Station it should be assigned to.
     * @return Updated Vehicle information.
     * @throws MissingNodeException Gets thrown if Node could not be found in repository.
     */
    public VehicleDTO stop(VehicleAtStationDTO vehicleAtStationDTO) throws MissingNodeException {

        Long vehicleId = vehicleAtStationDTO.getId();
        Optional<Station> stationToSet = stationRepository.findStationByStopId(vehicleAtStationDTO.getStation().getStopId());
        Optional<Vehicle> optionalVehicleFromDB = vehicleRepository.findById(vehicleId);

        if (optionalVehicleFromDB.isEmpty() || stationToSet.isEmpty()) {
            throw new MissingNodeException("Either the Station or Vehicle does not exist in the repository");
        }
        Vehicle vehicleFromDB = optionalVehicleFromDB.get();
        vehicleFromDB.setStation(stationToSet.get());
        Vehicle updatedVehicle = vehicleRepository.save(vehicleFromDB);
        return DTOGenerator.vehicleDTOFromVehicle(updatedVehicle);
    }

    /**
     * Retrieves all Vehicles and their locations from the Database
     *
     * @return String of all Vehicles and their locations.
     */
    public String retrieveVehicleInformation() {
        List<String> vehicleList = new ArrayList<>();
        Iterable<Vehicle> allVehicles = vehicleRepository.findAll();
        return vehicleStringBuilder(allVehicles);
    }

    /**
     * Helper method to build a Plaintext message from a List of Vehicles.
     *
     * @param allVehicles List of all Vehicles.
     * @return String of all Vehicles and their locations.
     */
    public static String vehicleStringBuilder(Iterable<Vehicle> allVehicles) {
        List<String> vehicleList = new ArrayList<>();
        allVehicles.forEach(vehicle -> vehicleList.add(DTOGenerator.vehicleDTOFromVehicle(vehicle).toString()));
        AtomicReference<String> plainText = new AtomicReference<>("List of Vehicles:");
        vehicleList.forEach(item -> plainText.set(plainText.get().concat(item + " ")));
        return plainText.get();
    }

    /**
     * Creates a Vehicle in the Database
     *
     * @param vehicleDTO The Vehicle to insert
     * @return The generated Vehicle.
     */
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        if (vehicleDTO.getId() != null) {
            vehicleDTO.setId(null);
        }
        if (vehicleDTO.getStation() == null && vehicleDTO.getName() == null)
            throw new IllegalArgumentException("Vehicle is completely empty");

        return DTOGenerator.vehicleDTOFromVehicle(
                vehicleRepository.save(VehicleGenerator.createVehicleFromDTO(vehicleDTO)));
    }
}