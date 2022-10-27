package de.fhkiel.advancedjava.restservice;


import de.fhkiel.advancedjava.businesslogic.DisturbanceLogic;
import de.fhkiel.advancedjava.dto.DisturbanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = API.DISTURBANCE)
public class DisturbanceController {

    private final DisturbanceLogic disturbanceLogic;

    @Autowired
    public DisturbanceController(DisturbanceLogic disturbanceLogic) {
        this.disturbanceLogic = disturbanceLogic;
    }

    /**
     * Creates a disturbance on a given Station or Section.
     *
     * @param disturbanceDTO the Format the JsonFile should be based on.
     * @return ResponseEntity with feedback of state.
     */
    @PatchMapping(path = API.NEW, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<String> createNewDisturbance(@RequestBody DisturbanceDTO disturbanceDTO) {
        try {
            disturbanceLogic.createDisturbance(disturbanceDTO);
            return ResponseEntity.accepted().body("Disturbance has been created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Changes the disturbance state on a Station or Section.
     *
     * @param disturbanceDTO the Format the JsonFile should be based on.
     * @return ResponseEntity feedback of state
     */
    @PatchMapping(path = API.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<String> deleteDisturbance(@RequestBody DisturbanceDTO disturbanceDTO) {
        try {
            disturbanceLogic.removeDisturbance(disturbanceDTO);
            return ResponseEntity.accepted().body("Disturbance has been deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
