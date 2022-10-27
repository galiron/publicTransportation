package de.fhkiel.advancedjava.restservice;


import de.fhkiel.advancedjava.businesslogic.StationLogic;
import de.fhkiel.advancedjava.dto.OnlyStationStateDTO;
import de.fhkiel.advancedjava.dto.StationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = API.STATION)
public class StationController {

    private final StationLogic stationLogic;

    @Autowired
    public StationController(StationLogic stationLogic) {
        this.stationLogic = stationLogic;
    }

    /**
     * Creates a new Station (initially CLOSED and not disturbed) from JsonFile.
     *
     * @param stationRegistrationDTO the Format the JsonFile should be based on.
     * @return ResponseEntity with generated station as body (if successful).
     */
    @PutMapping(path = API.NEW, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<StationDTO> createNewStation(@RequestBody StationDTO stationRegistrationDTO) {
        try {
            //return ok instead of accepted to be able to view the body
            return ResponseEntity.ok(stationLogic.createStop(stationRegistrationDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Imports a Station from JsonFile.
     *
     * @param stationRegistrationDTO the Format the JsonFile should be based on.
     * @return ResponseEntity with imported station as body (if successful).
     */
    @PutMapping(path = API.IMPORT, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<StationDTO> importNewStation(@RequestBody StationDTO stationRegistrationDTO) {
        try {
            //return ok instead of accepted to be able to view the body
            return ResponseEntity.ok(stationLogic.importStop(stationRegistrationDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Changes the State Of a Station.
     *
     * @param onlyStationStateDTO the Format the JsonFile should be based on.
     * @return ResponseEntity with changed station entity as body (if successful).
     */
    @PatchMapping(path = API.CHANGE_STATE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<StationDTO> changeStationState(@RequestBody OnlyStationStateDTO onlyStationStateDTO) {
        try {
            return ResponseEntity.ok(stationLogic.changeStationState(onlyStationStateDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
