package de.fhkiel.advancedjava.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import de.fhkiel.advancedjava.BaseTest;
import de.fhkiel.advancedjava.application.PublicTransportationPlatformApplication;
import de.fhkiel.advancedjava.dto.StationTupleDTO;
import de.fhkiel.advancedjava.dto.TrafficLineDTO;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.entities.nodes.TrafficLine;
import de.fhkiel.advancedjava.enums.MeanOfTransport;
import de.fhkiel.advancedjava.enums.StationState;
import de.fhkiel.advancedjava.generator.DTOGenerator;
import de.fhkiel.advancedjava.generator.SectionGenerator;
import de.fhkiel.advancedjava.generator.TrafficLineGenerator;
import de.fhkiel.advancedjava.testObjectGenerators.MeansOfTransportGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("TrafficLineControllerTest")
@AutoConfigureMockMvc
@SpringBootTest(classes = PublicTransportationPlatformApplication.class)
public class TrafficLineControllerTest extends BaseTest {

    @Test
    void createNewTrafficLine() throws Exception {
        Station station1 = createStation(1L, "stop1", "city1", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 1, false, 0);
        Station station2 = createStation(2L, "stop2", "city2", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 2, false, 1);
        Station station3 = createStation(2L, "shouldNotAppear", "Shouldn't be saved", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 2, false, 0);
        Station station4 = createStation(3L, "stop3", "city2", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 3, false, 1);

        TrafficLine trafficLine = TrafficLineGenerator.customTrafficLine(null, 1L, "testLine", MeanOfTransport.BUS,
                List.of(
                        // Stations need to be preset in the repository in order to create TrafficLines
                        SectionGenerator.customSection(null,
                                station1,
                                station2,
                                3,
                                false,
                                0),
                        SectionGenerator.customSection(null,
                                station3,
                                station4,
                                3,
                                false,
                                0)));

        TrafficLineDTO trafficLineDTO = DTOGenerator.trafficLineDTOFromTrafficLine(trafficLine);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(trafficLineDTO);

        this.mockMvc.perform(put(API.TRAFFIC_LINE + API.NEW)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        SortedSet<TrafficLine> trafficLines = trafficLineRepository.findAll(2);
        Assertions.assertThat(trafficLines.size()).isEqualTo(1);
        TrafficLine trafficLineAfter = trafficLines.first();
        TrafficLineDTO trafficLineDTOAfter = DTOGenerator.trafficLineDTOFromTrafficLine(trafficLineAfter);
        Assertions.assertThat(trafficLineDTOAfter.getName()).isEqualTo(trafficLineDTO.getName());
        Assertions.assertThat(trafficLineDTOAfter.getSections().size()).isEqualTo(Iterators.size(sectionRepository.findAll().iterator()));
        Assertions.assertThat(trafficLineDTOAfter.getSections()).isEqualTo(trafficLineDTO.getSections());
        Assertions.assertThat(trafficLineDTOAfter.getLineId()).isEqualTo(trafficLineDTO.getLineId());

        trafficLineAfter.getSections().forEach(section -> {
            // check if station supports the type of traffic line transportation.
            Assertions.assertThat(section.getStartStation().getMeansOfTransport()).contains(trafficLineAfter.getMeanOfTransport());
            Assertions.assertThat(section.getEndStation().getMeansOfTransport()).contains(trafficLineAfter.getMeanOfTransport());
        });
    }

    @Test
    void createInvalidUnsupportedTrafficLine() throws Exception {

        TrafficLine trafficLine = TrafficLineGenerator.customTrafficLine(null, 1L, "testLine", MeanOfTransport.SUBWAY,
                List.of(
                        // Stations need to be preset in the repository in order to create TrafficLines
                        SectionGenerator.customSection(null,
                                createStation(1L, "stop1", "city1", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 1, false, 0),
                                createStation(2L, "stop2", "city2", MeansOfTransportGenerator.generateOnlyBus(), StationState.CLOSED, 2, false, 1),
                                3,
                                false,
                                0),
                        SectionGenerator.customSection(null,
                                createStation(2L, "shouldNotAppear", "Shouldn't be saved", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 2, false, 0),
                                createStation(3L, "stop3", "city2", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 3, false, 1),
                                3,
                                false,
                                0)));
        TrafficLineDTO trafficLineDTO = DTOGenerator.trafficLineDTOFromTrafficLine(trafficLine);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(trafficLineDTO);

        this.mockMvc.perform(put(API.TRAFFIC_LINE + API.NEW)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewTrafficLineWithExistingSection() throws Exception {
        Station station1 = createStation(1L, "stop1", "city1", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 1, false, 0);
        Station station2 = createStation(2L, "stop2", "city2", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 2, false, 1);
        Station station3 = createStation(2L, "shouldNotAppear", "Shouldn't be saved", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 2, false, 0);
        Station station4 = createStation(3L, "stop3", "city2", MeansOfTransportGenerator.ALL_TYPES, StationState.CLOSED, 3, false, 1);

        Section section1 = createSection(null, station1, station2, 3, false, 0);
        TrafficLine trafficLine = TrafficLineGenerator.customTrafficLine(null, 1L, "testLine", MeanOfTransport.BUS,
                List.of(
                        // Stations need to be preset in the repository in order to create TrafficLines
                        section1,
                        SectionGenerator.customSection(null,
                                station3,
                                station4,
                                3,
                                false,
                                0)));

        TrafficLineDTO trafficLineDTO = DTOGenerator.trafficLineDTOFromTrafficLine(trafficLine);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(trafficLineDTO);

        this.mockMvc.perform(put(API.TRAFFIC_LINE + API.NEW)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        SortedSet<TrafficLine> trafficLines = trafficLineRepository.findAll(2);
        Assertions.assertThat(trafficLines.size()).isEqualTo(1);
        TrafficLine trafficLineAfter = trafficLines.first();
        TrafficLineDTO trafficLineDTOAfter = DTOGenerator.trafficLineDTOFromTrafficLine(trafficLineAfter);
        Assertions.assertThat(trafficLineDTOAfter.getName()).isEqualTo(trafficLineDTO.getName());
        Assertions.assertThat(trafficLineDTOAfter.getSections().size()).isEqualTo(Iterators.size(sectionRepository.findAll().iterator()));
        Assertions.assertThat(trafficLineDTOAfter.getSections()).isEqualTo(trafficLineDTO.getSections());
        Assertions.assertThat(trafficLineDTOAfter.getLineId()).isEqualTo(trafficLineDTO.getLineId());

        trafficLineAfter.getSections().forEach(section -> {
            // check if station supports the type of traffic line transportation.
            Assertions.assertThat(section.getStartStation().getMeansOfTransport()).contains(trafficLineAfter.getMeanOfTransport());
            Assertions.assertThat(section.getEndStation().getMeansOfTransport()).contains(trafficLineAfter.getMeanOfTransport());
        });
    }


    @Test
    void findFastestConnection() throws Exception {

        // the test for this call is already covered in BackupControllerTest
        // just used here to get TrafficLines for testing into the db
        ObjectMapper mapper = new ObjectMapper();
        File path = new File("src/test/resources/testFiles/shortestPath.json");
        String jsonString = mapper.writeValueAsString(mapper.readValue(path, Object.class));
        this.mockMvc.perform(post(API.BACKUP + API.IMPORT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isAccepted());

        Iterable<Station> stationsFromDB = stationRepository.findAll();
        Iterator<Station> dbIterator = stationsFromDB.iterator();
        Station startStation = dbIterator.next();
        dbIterator.next();
        dbIterator.next();
        dbIterator.next();
        Station endStation = dbIterator.next();
        dbIterator.next();

        System.out.println("StartStation = " + startStation);
        System.out.println("EndStation = " + endStation);

        StationTupleDTO stationTupleDTO = DTOGenerator.customStationDupleDTO(
                DTOGenerator.stationDTOFromStation(startStation)
                , DTOGenerator.stationDTOFromStation(endStation));

        jsonString = mapper.writeValueAsString(stationTupleDTO);

        this.mockMvc.perform(get(API.TRAFFIC_LINE + API.FIND_FASTEST_CONNECTION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTravelTime", is(12)));
    }

}
