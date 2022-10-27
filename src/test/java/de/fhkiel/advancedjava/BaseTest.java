package de.fhkiel.advancedjava;

import de.fhkiel.advancedjava.application.PublicTransportationPlatformApplication;
import de.fhkiel.advancedjava.entities.nodes.*;
import de.fhkiel.advancedjava.enums.MeanOfTransport;
import de.fhkiel.advancedjava.enums.StationState;
import de.fhkiel.advancedjava.generator.*;
import de.fhkiel.advancedjava.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.SortedSet;

/**
 * Superclass for all tests, implementing useful functions and necessary repositories.
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = PublicTransportationPlatformApplication.class)
public class BaseTest {

    @Autowired
    private WebApplicationContext webapplicationContext;

    @Autowired
    protected MockMvc mockMvc;


    @Autowired
    protected StationRepository stationRepository;

    @Autowired
    protected SectionRepository sectionRepository;

    @Autowired
    protected TrafficLineRepository trafficLineRepository;

    @Autowired
    protected VehicleRepository vehicleRepository;

    @Autowired
    protected TicketRepository ticketRepository;


    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webapplicationContext).build();
        clearDB();
    }

    @AfterEach
    public void tearDown() {
        //code here for cleanup
        //clearDB();
    }

    protected Section createSection(Long id, Station startStation, Station endStation, Integer durationInMinutes, Boolean disturbed, Integer disturbanceCounter) {
        Section section = SectionGenerator.customSection(id, startStation, endStation, durationInMinutes, disturbed, disturbanceCounter);
        return sectionRepository.save(section);
    }

    protected Station createStation(Long stopId, String name, String city, List<MeanOfTransport> meanOfTransports, StationState state, Integer connectionTimeInMin, Boolean disturbed, Integer disturbanceCounter) {
        SortedSet<Station> station = stationRepository.findByStopId(stopId);
        if (station.isEmpty())
            return stationRepository.save(StationGenerator.customStation(null, stopId, name, city, meanOfTransports, state, connectionTimeInMin, disturbed, disturbanceCounter));
        else return station.first();
    }

    protected TrafficLine createTrafficLine(Long lineId, String name, MeanOfTransport meanOfTransport, List<Section> sections) {
        return trafficLineRepository.save(TrafficLineGenerator.customTrafficLine(null, lineId,  name, meanOfTransport, sections));
    }

    protected Vehicle createVehicle(Long id, String name, Station station) {
        return vehicleRepository.save(VehicleGenerator.customVehicle(id, name, station));
    }

    protected Ticket createTicket(Long id, Double price,Section section) {
        return ticketRepository.save(TicketGenerator.customTicket(id,price,section));
    }


    private void clearDB() {
        trafficLineRepository.deleteAll();
        sectionRepository.deleteAll();
        stationRepository.deleteAll();
        vehicleRepository.deleteAll();
        ticketRepository.deleteAll();
    }
}
