package de.fhkiel.advancedjava.businesslogic;

import de.fhkiel.advancedjava.dto.SectionDTO;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.generator.SectionGenerator;
import de.fhkiel.advancedjava.repository.SectionRepository;
import de.fhkiel.advancedjava.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SectionLogic {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    @Autowired
    public SectionLogic(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    /**
     * Creates a Section in the repository.
     * Fails if a station does not exist.
     *
     * @param sectionDTO Containing information about the Section to be created.
     * @return Created Section.
     */
    public Section createSection(SectionDTO sectionDTO) {
        Optional<Station> maybeStartStation = stationRepository.findStationByStopId(sectionDTO.getBeginStopId());
        Optional<Station> maybeEndStation = stationRepository.findStationByStopId(sectionDTO.getEndStopId());

        if (maybeStartStation.isPresent() && maybeEndStation.isPresent()) {
            Section sectionToSave = SectionGenerator.createSectionFromDTO(null, maybeStartStation.get()
                    , maybeEndStation.get()
                    , sectionDTO);
            return sectionRepository.save(sectionToSave);
        }
        throw new IllegalStateException("Station doesn't exist");
    }
}
