package de.fhkiel.advancedjava.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhkiel.advancedjava.BaseTest;
import de.fhkiel.advancedjava.application.PublicTransportationPlatformApplication;
import de.fhkiel.advancedjava.dto.OnlyStationStateDTO;
import de.fhkiel.advancedjava.dto.StationDTO;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.enums.StationState;
import de.fhkiel.advancedjava.generator.DTOGenerator;
import de.fhkiel.advancedjava.testObjectGenerators.MeansOfTransportGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.SortedSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("StationControllerTest")
@AutoConfigureMockMvc
@SpringBootTest(classes = PublicTransportationPlatformApplication.class)
public class StationControllerTest extends BaseTest {

    @Test
    void createNewStation() throws Exception {
        StationDTO stationDTO = DTOGenerator.customStationDTO(
                null
                , 2L
                , "testName"
                , "testCity"
                , MeansOfTransportGenerator.ALL_TYPES
                , StationState.CLOSED
                , 1
                , true
                , 2);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(stationDTO);


        this.mockMvc.perform(put(API.STATION + API.NEW)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        SortedSet<Station> station = stationRepository.findByName(stationDTO.getName());
        Assertions.assertThat(station.size()).isEqualTo(1);
        Station realStation = station.first();
        Assertions.assertThat(realStation.getCity()).isEqualTo(stationDTO.getCity());
        //new generated Station should always be closed
        Assertions.assertThat(realStation.getStationState()).isEqualTo(StationState.OPEN);
        Assertions.assertThat(realStation.getDisturbanceCounter()).isEqualTo(0);
        Assertions.assertThat(realStation.isDisturbed()).isEqualTo(false);
        Assertions.assertThat(realStation.getMeansOfTransport()).isEqualTo(stationDTO.getTypes());
    }

    @Test
    void importStation() throws Exception {
        StationDTO stationDTO = DTOGenerator.customStationDTO(
                null
                , 2L
                , "testName"
                , "testCity"
                , MeansOfTransportGenerator.ALL_TYPES
                , StationState.OPEN
                , 1
                , false
                , 0);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(stationDTO);


        this.mockMvc.perform(put(API.STATION + API.IMPORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        SortedSet<Station> station = stationRepository.findByStopId(stationDTO.getStopId());
        Assertions.assertThat(station.size()).isEqualTo(1);
        Station realStation = station.first();
        Assertions.assertThat(realStation.getStopId()).isEqualTo(stationDTO.getStopId());
        Assertions.assertThat(realStation.getCity()).isEqualTo(stationDTO.getCity());
        //new generated Station should always be closed
        Assertions.assertThat(realStation.getStationState()).isEqualTo(stationDTO.getState());
        Assertions.assertThat(realStation.getMeansOfTransport()).isEqualTo(stationDTO.getTypes());
    }

    @Test
    void changeStationState() throws Exception {
        StationDTO stationDTO = DTOGenerator.customStationDTO(null, 2L, "501", "Kiel", MeansOfTransportGenerator.ALL_TYPES, StationState.OPEN, 2, false, 0);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(stationDTO);

        this.mockMvc.perform(put(API.STATION + API.IMPORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        OnlyStationStateDTO onlyStationStateDTO = DTOGenerator.customOnlyStationStateDTO(2L,StationState.CLOSED);
        jsonString = mapper.writeValueAsString(onlyStationStateDTO);

        SortedSet<Station> stationBeforeChange = stationRepository.findByStopId(onlyStationStateDTO.getStopId());
        Station stationBefore = stationBeforeChange.first();

        this.mockMvc.perform(patch(API.STATION + API.CHANGE_STATE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        SortedSet<Station> stationAfterChange = stationRepository.findByStopId(onlyStationStateDTO.getStopId());
        Assertions.assertThat(stationAfterChange.size()).isEqualTo(stationBeforeChange.size());
        Station stationAfter = stationAfterChange.first();
        Assertions.assertThat(stationAfter.getId()).isEqualTo(stationBefore.getId());
        Assertions.assertThat(stationAfter.getStopId()).isEqualTo(stationBefore.getStopId());
        Assertions.assertThat(stationAfter.getCity()).isEqualTo(stationBefore.getCity());
        //new generated Station should always be closed
        Assertions.assertThat(stationAfter.getStationState()).isNotEqualTo(stationBefore.getStationState());
        Assertions.assertThat(stationAfter.getMeansOfTransport()).isEqualTo(stationBefore.getMeansOfTransport());
    }

}
