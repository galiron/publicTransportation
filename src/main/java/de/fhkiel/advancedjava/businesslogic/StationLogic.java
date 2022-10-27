package de.fhkiel.advancedjava.businesslogic;


import de.fhkiel.advancedjava.dto.OnlyStationStateDTO;
import de.fhkiel.advancedjava.dto.StationDTO;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.generator.DTOGenerator;
import de.fhkiel.advancedjava.generator.StationGenerator;
import de.fhkiel.advancedjava.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StationLogic {

    private final StationRepository stationRepository;

    @Autowired
    public StationLogic(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    /**
     * Creates a new Station in the repository.
     *
     * @param stationRegistrationDTO Containing information about the Station to be created.
     * @return Created Station
     */
    public StationDTO createStop(StationDTO stationRegistrationDTO) {
        Station stationToCreate = StationGenerator.defaultStation(
                stationRegistrationDTO.getStopId(),
                stationRegistrationDTO.getName(),
                stationRegistrationDTO.getCity(),
                stationRegistrationDTO.getTypes());
        Station newStation = stationRepository.save(stationToCreate);

        return DTOGenerator.stationDTOFromStation(newStation);
    }

    public StationDTO importStop(StationDTO stationRegistrationDTO) {
        Station stationToCreate = StationGenerator.createStationFromDto(stationRegistrationDTO);

        Station newStation = stationRepository.save(stationToCreate);

        return DTOGenerator.stationDTOFromStation(newStation);
    }

    public StationDTO changeStationState(OnlyStationStateDTO onlyStationStateDTO) {
        Optional<Station> optionalStationFromId = stationRepository.findStationByStopId(onlyStationStateDTO.getStopId());
        if (optionalStationFromId.isEmpty()) {
            throw new IllegalStateException("station couldn't be found");
        }
        Station stationFromId = optionalStationFromId.get();

        if (onlyStationStateDTO.getStationState() != null) {
            stationFromId.setStationState(onlyStationStateDTO.getStationState());
            Station stateWithChangedState = stationRepository.save(stationFromId);
            return DTOGenerator.stationDTOFromStation(stateWithChangedState);
        } else {
            throw new IllegalStateException("The state is not valid");
        }
    }
}
