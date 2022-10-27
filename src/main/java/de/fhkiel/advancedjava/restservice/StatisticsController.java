package de.fhkiel.advancedjava.restservice;

import de.fhkiel.advancedjava.businesslogic.StatisticsLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private final StatisticsLogic statisticslogic;

    @Autowired
    public StatisticsController(StatisticsLogic statisticsLogic) {
        this.statisticslogic = statisticsLogic;
    }

    /**
     * Generates a Statistic from the Neo4JGraph.
     *
     * @return ResponseEntity with statistics as body (if successful).
     */
    @GetMapping(path = API.STATISTICS, produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> getStatistics() {
        try {
            return ResponseEntity.ok(statisticslogic.getStatistics());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
