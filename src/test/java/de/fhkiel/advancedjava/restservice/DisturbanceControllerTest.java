package de.fhkiel.advancedjava.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhkiel.advancedjava.BaseTest;
import de.fhkiel.advancedjava.application.PublicTransportationPlatformApplication;
import de.fhkiel.advancedjava.dto.DisturbanceDTO;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.enums.DisturbanceType;
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

import java.util.Optional;
import java.util.SortedSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("DisturbanceControllerTest")
@AutoConfigureMockMvc
@SpringBootTest(classes = PublicTransportationPlatformApplication.class)
public class DisturbanceControllerTest extends BaseTest {

    @Test
    void createNewDisturbanceOnStation() throws Exception {
        Station station = createStation(1L,"test", "testCity", MeansOfTransportGenerator.ALL_TYPES, StationState.OPEN, 0,false, 0);
        DisturbanceDTO disturbanceDTO = DTOGenerator.customDisturbanceDTO(station.getId(), DisturbanceType.STATION);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(disturbanceDTO);

        this.mockMvc.perform(patch(API.DISTURBANCE + API.NEW)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isAccepted());

        SortedSet<Station> stationAfter = stationRepository.findByStopId(station.getStopId());
        Assertions.assertThat(stationAfter.size()).isEqualTo(1);
        Station realStation = stationAfter.first();
        Assertions.assertThat(realStation.getStopId()).isEqualTo(station.getStopId());
        Assertions.assertThat(realStation.getCity()).isEqualTo(station.getCity());
        //new generated Station should always be closed
        Assertions.assertThat(realStation.getStationState()).isEqualTo(station.getStationState());
        Assertions.assertThat(realStation.getMeansOfTransport()).isEqualTo(station.getMeansOfTransport());
        Assertions.assertThat(realStation.isDisturbed()).isNotEqualTo(station.isDisturbed());
        Assertions.assertThat(realStation.getDisturbanceCounter()).isEqualTo(1);
    }

    @Test
    void createNewDisturbanceOnSection() throws Exception {
        Section section = createSection(null,
                createStation(1L, "1", "testCity1", MeansOfTransportGenerator.ALL_TYPES, StationState.OPEN, 0, false, 0),
                createStation(2L, "2", "testCity2", MeansOfTransportGenerator.ALL_TYPES, StationState.OPEN, 0, false, 0),
                 2, false, 1);
        DisturbanceDTO disturbanceDTO = DTOGenerator.customDisturbanceDTO(section.getId(), DisturbanceType.SECTION);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(disturbanceDTO);

        this.mockMvc.perform(patch(API.DISTURBANCE + API.NEW)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isAccepted());

        Optional<Section> sectionAfter = sectionRepository.findById(section.getId());
        if(sectionAfter.isEmpty()) throw new MissingNodeException("section could not be found by given ID");
        Assertions.assertThat(sectionAfter.get().getStartStation()).isEqualTo(section.getStartStation());
        Assertions.assertThat(sectionAfter.get().getEndStation()).isEqualTo(section.getEndStation());
        Assertions.assertThat(sectionAfter.get().getDisturbanceCounter()).isEqualTo(section.getDisturbanceCounter()+1);
        Assertions.assertThat(sectionAfter.get().getDisturbed()).isNotEqualTo(section.getDisturbed());
        Assertions.assertThat(sectionAfter.get().getDurationInMinutes()).isEqualTo(section.getDurationInMinutes());
    }


    @Test
    void deleteDisturbanceOnStation() throws Exception {
        Station station = createStation(1L, "test", "testCity", MeansOfTransportGenerator.ALL_TYPES, StationState.OPEN, 0, true, 1);
        DisturbanceDTO disturbanceDTO = DTOGenerator.customDisturbanceDTO(station.getId(), DisturbanceType.STATION);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(disturbanceDTO);

        this.mockMvc.perform(patch(API.DISTURBANCE + API.DELETE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isAccepted());

        SortedSet<Station> stationAfter = stationRepository.findByStopId(station.getStopId());
        Assertions.assertThat(stationAfter.size()).isEqualTo(1);
        Station realStation = stationAfter.first();
        Assertions.assertThat(realStation.getStopId()).isEqualTo(station.getStopId());
        Assertions.assertThat(realStation.getCity()).isEqualTo(station.getCity());
        //new generated Station should always be closed
        Assertions.assertThat(realStation.getStationState()).isEqualTo(station.getStationState());
        Assertions.assertThat(realStation.getMeansOfTransport()).isEqualTo(station.getMeansOfTransport());
        Assertions.assertThat(realStation.isDisturbed()).isNotEqualTo(station.isDisturbed());
    }

    @Test
    void deleteDisturbanceOnSection() throws Exception {
        Section section = createSection(null,
                createStation(1L, "1", "testCity1", MeansOfTransportGenerator.ALL_TYPES, StationState.OPEN, 0, false, 0),
                createStation(2L, "2", "testCity2", MeansOfTransportGenerator.ALL_TYPES, StationState.OPEN, 0, false, 0),
                0, true, 1);
        DisturbanceDTO disturbanceDTO = DTOGenerator.customDisturbanceDTO(section.getId(), DisturbanceType.SECTION);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(disturbanceDTO);

        this.mockMvc.perform(patch(API.DISTURBANCE + API.DELETE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isAccepted());

        Optional<Section> sectionAfter = sectionRepository.findById(section.getId());
        if(sectionAfter.isEmpty()) throw new MissingNodeException("section could not be found by given ID");
        Assertions.assertThat(sectionAfter.get().getStartStation()).isEqualTo(section.getStartStation());
        Assertions.assertThat(sectionAfter.get().getEndStation()).isEqualTo(section.getEndStation());
        Assertions.assertThat(sectionAfter.get().getDisturbanceCounter()).isEqualTo(section.getDisturbanceCounter());
        Assertions.assertThat(sectionAfter.get().getDisturbed()).isNotEqualTo(section.getDisturbed());
        Assertions.assertThat(sectionAfter.get().getDurationInMinutes()).isEqualTo(section.getDurationInMinutes());
    }
}
