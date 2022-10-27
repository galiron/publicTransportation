package de.fhkiel.advancedjava.restservice;


import de.fhkiel.advancedjava.businesslogic.VehicleLogic;
import de.fhkiel.advancedjava.dto.VehicleAtStationDTO;
import de.fhkiel.advancedjava.dto.VehicleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = API.VEHICLE)
public class VehicleController {

    private final VehicleLogic vehicleLogic;

    @Autowired
    public VehicleController(VehicleLogic vehicleLogic) {
        this.vehicleLogic = vehicleLogic;
    }

    /**
     * Assigns a Vehicle to a Station.
     *
     * @param vehicleAtStationDTO The Format the JsonFile should be based on.
     * @return ResponseEntity with vehicle assigned to a station as body (if successful).
     */
    @PatchMapping(path = API.STOP, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<VehicleDTO> stopAtStation(@RequestBody VehicleAtStationDTO vehicleAtStationDTO) {
        try {
            return ResponseEntity.ok(vehicleLogic.stop(vehicleAtStationDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves the location of Vehicles.
     *
     * @return ResponseEntity with plaintext as body (if successful).
     */
    @GetMapping(path = API.RETRIEVE_LOCATIONS, produces = {MediaType.TEXT_PLAIN_VALUE})
    public @ResponseBody
    ResponseEntity<String> getVehicleLocationList() {
        try {
            return ResponseEntity.ok(vehicleLogic.retrieveVehicleInformation());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Creates a vehicle in the repository.
     *
     * @param vehicleDTO The Format the JsonFile should be based on.
     * @return The newly generated Vehicle (if successful).
     */
    @PutMapping(path = API.NEW, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<VehicleDTO> createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        try {
            VehicleDTO ve = vehicleLogic.createVehicle(vehicleDTO);
            return ResponseEntity.ok(ve);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
