package de.fhkiel.advancedjava.restservice;


import de.fhkiel.advancedjava.businesslogic.TrafficLineLogic;
import de.fhkiel.advancedjava.dto.ShortestPathDTO;
import de.fhkiel.advancedjava.dto.StationTupleDTO;
import de.fhkiel.advancedjava.dto.TrafficLineDTO;
import de.fhkiel.advancedjava.generator.StationGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = API.TRAFFIC_LINE)
public class TrafficLineController {

    private final TrafficLineLogic trafficLineLogic;

    @Autowired
    public TrafficLineController(TrafficLineLogic trafficLineLogic){
        this.trafficLineLogic = trafficLineLogic;
    }

    /**
     * Creates a new traffic line.
     * @param trafficLineDto The Format the JsonFile should be based on.
     * @return ResponseEntity with created traffic line as body (if successful).
     */
    @PutMapping(path = API.NEW, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<TrafficLineDTO> createNewTrafficLine(@RequestBody TrafficLineDTO trafficLineDto) {
        try {
            return ResponseEntity.ok(trafficLineLogic.createTrafficLine(trafficLineDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = API.FIND_FASTEST_CONNECTION, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<ShortestPathDTO> findFastestConnection(@RequestBody StationTupleDTO stationTupleDTO){
//        try{
            return ResponseEntity.ok(trafficLineLogic.getFastestConnection(StationGenerator.createStationFromDto(stationTupleDTO.getStationA()), StationGenerator.createStationFromDto(stationTupleDTO.getStationB())));
//        }
//        catch (Exception e){
//            return ResponseEntity.badRequest().build();
//        }
    }
}
