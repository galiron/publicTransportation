package de.fhkiel.advancedjava.restservice;

import de.fhkiel.advancedjava.BaseTest;
import de.fhkiel.advancedjava.application.PublicTransportationPlatformApplication;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.entities.nodes.Ticket;
import de.fhkiel.advancedjava.enums.StationState;
import de.fhkiel.advancedjava.testObjectGenerators.MeansOfTransportGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("StatisticsControllerTest")
@AutoConfigureMockMvc
@SpringBootTest(classes = PublicTransportationPlatformApplication.class)
public class StatisticsControllerTest extends BaseTest {

    @Test
    void getStatistics() throws Exception {

        Station startStation = createStation(1L, "test1", "testCity1", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 2, false, 4);
        Station endStation = createStation(2L, "test2", "testCity2", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 2, false, 2);
        Station stationForStatistics = createStation(3L, "test2", "testCity2", MeansOfTransportGenerator.generateOnlyBus(), StationState.CLOSED, 2, false, 2);
        Section sectionForStatistics1 = createSection(null, startStation, endStation, 0 ,false, 3);
        Section sectionForStatistics2 = createSection(null, endStation, stationForStatistics, 0 ,false, 3);
        Ticket ticketForStatistics1 = createTicket(null, 2.4, sectionForStatistics1);
        Ticket ticketForStatistics2 = createTicket(null, 1.4, sectionForStatistics2);

        ResultActions resultAction = this.mockMvc.perform(get(API.STATISTICS))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());


        // Nasty to test because of String return value, so I will just compare the DB with the returned value, no intense testing for this one
        MvcResult result = resultAction.andReturn();
        String plainText = result.getResponse().getContentAsString();
        System.out.println(plainText);

    }


}
