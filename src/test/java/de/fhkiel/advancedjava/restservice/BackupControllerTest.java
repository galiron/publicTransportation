package de.fhkiel.advancedjava.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhkiel.advancedjava.BaseTest;
import de.fhkiel.advancedjava.application.PublicTransportationPlatformApplication;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.entities.nodes.TrafficLine;
import de.fhkiel.advancedjava.enums.MeanOfTransport;
import de.fhkiel.advancedjava.enums.StationState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = PublicTransportationPlatformApplication.class)
public class BackupControllerTest extends BaseTest {

    @Test
    void loadBackup() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        File path = new File("src/test/resources/testFiles/dbImport.json");
        String jsonString = mapper.writeValueAsString(mapper.readValue(path,Object.class));
        this.mockMvc.perform(post(API.BACKUP + API.IMPORT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.stops", hasSize(6)))
                .andExpect(jsonPath("$.trafficLines", hasSize(2)))
                .andExpect(jsonPath("$.trafficLines[0].sections", hasSize(5)))
                .andExpect(jsonPath("$.trafficLines[1].sections", hasSize(2)));
    }

    @Test
    void createBackup_emptyDB() throws Exception {
        this.mockMvc.perform(get(API.BACKUP + API.EXPORT))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stops",hasSize(0)))
                .andExpect(jsonPath("$.trafficLines",hasSize(0)));
    }

    @Test
    void createBackup_withData() throws Exception {
        List<MeanOfTransport> meanOfTransports = new ArrayList<>();
        meanOfTransports.add(MeanOfTransport.BUS);
        meanOfTransports.add(MeanOfTransport.SUBWAY);
        Station station = createStation(1L, "Hummelwiese", "Kiel", meanOfTransports, StationState.OPEN, 0, false, 0);
        Station station2 = createStation(2L, "Olympia-Zentrum", "Strande", meanOfTransports, StationState.OPEN, 0, false, 0);
        Station station3 = createStation(3L, "Dummy", "Molfsee", meanOfTransports, StationState.OPEN, 0, false, 0);
        Section section = createSection(null, station, station2, 3, false, 0);
        Section section2 = createSection(null, station2, station3, 10, false, 0);
        List<Section> sections = new ArrayList<>();
        sections.add(section);
        sections.add(section2);

        TrafficLine trafficLine = createTrafficLine(1L,"501", MeanOfTransport.BUS, sections);
        TrafficLine trafficLine2 = createTrafficLine(2L,"502", MeanOfTransport.BUS, sections);

        this.mockMvc.perform(get(API.BACKUP + API.EXPORT))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stops", hasSize(3)))
                .andExpect(jsonPath("$.stops[0].stopId", is(station.getStopId().intValue())))
                .andExpect(jsonPath("$.stops[0].name", is(station.getName())))
                .andExpect(jsonPath("$.stops[0].city", is(station.getCity())))
                .andExpect(jsonPath("$.stops[0].state", is(station.getStationState().toString())))
                .andExpect(jsonPath("$.stops[0].types", hasSize(station.getMeansOfTransport().size())))
                .andExpect(jsonPath("$.stops[0].types[0]", is(station.getMeansOfTransport().get(0).toString())))
                .andExpect(jsonPath("$.stops[0].types[1]", is(station.getMeansOfTransport().get(1).toString())))
                .andExpect(jsonPath("$.stops[0].types[1]", is(station.getMeansOfTransport().get(1).toString())))
                .andExpect(jsonPath("$.trafficLines", hasSize(2)))
                .andExpect(jsonPath("$.trafficLines[0].lineId", is(trafficLine.getLineId().intValue())))
                .andExpect(jsonPath("$.trafficLines[0].name", is(trafficLine.getName())))
                .andExpect(jsonPath("$.trafficLines[0].type", is(trafficLine.getMeanOfTransport().toString())))
                .andExpect(jsonPath("$.trafficLines[0].sections", hasSize(trafficLine.getSections().size())))
                .andExpect(jsonPath("$.trafficLines[0].sections[0].beginStopId", is(trafficLine.getSections().get(0).getStartStation().getStopId().intValue())))
                .andExpect(jsonPath("$.trafficLines[0].sections[0].endStopId", is(trafficLine.getSections().get(0).getEndStation().getStopId().intValue())))
                .andExpect(jsonPath("$.trafficLines[0].sections[0].durationInMinutes", is(trafficLine.getSections().get(0).getDurationInMinutes())));
    }
}
