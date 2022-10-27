package de.fhkiel.advancedjava.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import de.fhkiel.advancedjava.BaseTest;
import de.fhkiel.advancedjava.application.PublicTransportationPlatformApplication;
import de.fhkiel.advancedjava.businesslogic.VehicleLogic;
import de.fhkiel.advancedjava.dto.VehicleAtStationDTO;
import de.fhkiel.advancedjava.dto.VehicleDTO;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.entities.nodes.Vehicle;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("VehicleControllerTest")
@AutoConfigureMockMvc
@SpringBootTest(classes = PublicTransportationPlatformApplication.class)
public class VehicleControllerTest extends BaseTest {

    @Test
    void stopAtStation() throws Exception {
        //generating db
        Station initStation = createStation(2L
                , "501"
                , "Kiel"
                , MeansOfTransportGenerator.ALL_TYPES
                , StationState.OPEN
                , 2
                , false
                , 0);
        Station stationToSet = createStation(3L
                , "501"
                , "Kiel"
                , MeansOfTransportGenerator.ALL_TYPES
                , StationState.OPEN
                , 2
                , false
                , 0);
        Vehicle vehicle = createVehicle(null, "testVehicle", initStation);
        //setup dto for test
        VehicleAtStationDTO vehicleDTO = DTOGenerator.customVehicleAtStationDTO(vehicle.getId()
                , DTOGenerator.stationDTOFromStation(stationToSet));
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(vehicleDTO);

        this.mockMvc.perform(patch(API.VEHICLE + API.STOP)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        Optional<Vehicle> vehicleAfter = vehicleRepository.findById(vehicle.getId());
        if (vehicleAfter.isEmpty()) throw new MissingNodeException("Vehicle couldn't be found by given id");
        Assertions.assertThat(vehicle.getName()).isEqualTo(vehicleAfter.get().getName());
        Assertions.assertThat(vehicle.getStation()).isNotEqualTo(vehicleAfter.get().getStation());
        Assertions.assertThat(stationToSet).isEqualTo(vehicleAfter.get().getStation());
    }

    @Test
    void getVehicleLocationList() throws Exception {

        Station initStation = createStation(2L
                , "501"
                , "Kiel"
                , MeansOfTransportGenerator.ALL_TYPES
                , StationState.OPEN
                , 2
                , false
                , 0);
        Station stationToSet = createStation(3L
                , "501"
                , "Kiel"
                , MeansOfTransportGenerator.ALL_TYPES
                , StationState.OPEN
                , 2
                , false
                , 0);
        Vehicle vehicle = createVehicle(null, "testVehicle", initStation);
        Vehicle vehicle2 = createVehicle(null, "testVehicle2", initStation);

        ResultActions resultAction = this.mockMvc.perform(get(API.VEHICLE + API.RETRIEVE_LOCATIONS))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        MvcResult result = resultAction.andReturn();
        String plainText = result.getResponse().getContentAsString();
        System.out.println(plainText);

        Iterable<Vehicle> vehiclesAfter = vehicleRepository.findAll();
        Assertions.assertThat(VehicleLogic.vehicleStringBuilder(vehiclesAfter)).isEqualTo(plainText);

    }

    @Test
    void createNewVehicle() throws Exception {
        Station initStation = createStation(2L
                , "501"
                , "Kiel"
                , MeansOfTransportGenerator.ALL_TYPES
                , StationState.OPEN
                , 2
                , false
                , 0);

        VehicleDTO vehicleDTO = DTOGenerator.customVehicleDTO(2L, "testVehicle", DTOGenerator.stationDTOFromStation(initStation));
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(vehicleDTO);

        this.mockMvc.perform(put(API.VEHICLE + API.NEW)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        vehicleDTO = DTOGenerator.customVehicleDTO(null, "testVehicle", null);
        String jsonString2 = mapper.writeValueAsString(vehicleDTO);

        this.mockMvc.perform(put(API.VEHICLE + API.NEW)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString2))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        vehicleDTO = DTOGenerator.customVehicleDTO(2L, "testVehicle", null);
        String jsonString3 = mapper.writeValueAsString(vehicleDTO);

        this.mockMvc.perform(put(API.VEHICLE + API.NEW)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString3))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        vehicleDTO = DTOGenerator.customVehicleDTO(null, null, null);

        String jsonString4 = mapper.writeValueAsString(vehicleDTO);

        this.mockMvc.perform(put(API.VEHICLE + API.NEW)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonString4))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());



        Iterable<Vehicle> vehiclesAfter = vehicleRepository.findAll();
        Assertions.assertThat(Iterators.size(vehiclesAfter.iterator())).isEqualTo(3);
    }

}
