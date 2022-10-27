package de.fhkiel.advancedjava.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.fhkiel.advancedjava.BaseTest;
import de.fhkiel.advancedjava.application.PublicTransportationPlatformApplication;
import de.fhkiel.advancedjava.dto.TicketDTO;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.entities.nodes.Ticket;
import de.fhkiel.advancedjava.enums.StationState;
import de.fhkiel.advancedjava.exceptions.MissingNodeException;
import de.fhkiel.advancedjava.generator.DTOGenerator;
import de.fhkiel.advancedjava.testObjectGenerators.MeansOfTransportGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("TicketControllerTest")
@AutoConfigureMockMvc
@SpringBootTest(classes = PublicTransportationPlatformApplication.class)
public class TicketControllerTest extends BaseTest {

    @Test
    void buyTickets() throws Exception {

        Station startStation = createStation(1L, "stop1", "city1", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 1, false, 0);
        Station endStation = createStation(2L, "stop2", "city2", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 2, false, 1);
        Section section = createSection(null, startStation, endStation, 3, false, 0);

        TicketDTO ticket1 = DTOGenerator.customTicketDTO(null, 11.3, DTOGenerator.sectionDTOFromSection(section));
        TicketDTO ticket2 = DTOGenerator.customTicketDTO(null, 11.3, DTOGenerator.sectionDTOFromSection(section));

        List<TicketDTO> ticketDTOS = new ArrayList<>();
        ticketDTOS.add(ticket1);
        ticketDTOS.add(ticket2);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(ticketDTOS);

        AtomicReference<Integer> ticketId1 = new AtomicReference<>();
        AtomicReference<Integer> ticketId2 = new AtomicReference<>();

        this.mockMvc.perform(put(API.TICKET + API.BUY)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andDo(mvcResult -> {
                    String json = mvcResult.getResponse().getContentAsString();
                    ticketId1.set((JsonPath.read(json, "$[0].id")));
                    ticketId2.set((JsonPath.read(json, "$[0].id")));
                })
                .andExpect(status().isOk());


        Optional<Ticket> ticketFromDb1 = ticketRepository.findById(ticketId1.get().longValue());
        if (ticketFromDb1.isEmpty()) throw new MissingNodeException("Node could not be found by ID");
        Assertions.assertThat(ticketFromDb1.get().getPrice()).isEqualTo(ticket1.getPrice());
        Assertions.assertThat(ticketFromDb1.get().getSection().getId()).isEqualTo(ticket1.getSection().getSectionId());

        Optional<Ticket> ticketFromDb2 = ticketRepository.findById(ticketId2.get().longValue());
        if (ticketFromDb2.isEmpty()) throw new MissingNodeException("Node could not be found by ID");
        Assertions.assertThat(ticketFromDb2.get().getPrice()).isEqualTo(ticket2.getPrice());
        Assertions.assertThat(ticketFromDb2.get().getSection().getId()).isEqualTo(ticket2.getSection().getSectionId());
    }
}
