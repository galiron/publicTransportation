package de.fhkiel.advancedjava.businesslogic;


import de.fhkiel.advancedjava.dto.DisturbanceDTO;
import de.fhkiel.advancedjava.enums.DisturbanceType;
import de.fhkiel.advancedjava.exceptions.MissingNodeException;
import de.fhkiel.advancedjava.repository.SectionRepository;
import de.fhkiel.advancedjava.repository.StationRepository;
import de.fhkiel.advancedjava.entities.nodes.shared.Disturbable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DisturbanceLogic {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    @Autowired
    public DisturbanceLogic(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    /**
     * Creates a disturbance on a Node.
     *
     * @param disturbanceDTO Node to assign the disturbance.
     */
    public void createDisturbance(DisturbanceDTO disturbanceDTO) throws MissingNodeException {
        filterCategoryAndChangeDisturbance(disturbanceDTO, true);
    }

    /**
     * Removes a disturbance from a Node.
     *
     * @param disturbanceDTO Node from which the disturbance shall be removed.
     */
    public void removeDisturbance(DisturbanceDTO disturbanceDTO) throws MissingNodeException {
        filterCategoryAndChangeDisturbance(disturbanceDTO, false);
    }

    /**
     * Filters category and assigns the correct Repository to alter object.
     *
     * @param disturbanceDTO the object to be altered.
     * @param disturbed      the state the object should accept.
     */
    private void filterCategoryAndChangeDisturbance(DisturbanceDTO disturbanceDTO, Boolean disturbed) throws MissingNodeException {

        if (disturbanceDTO.getDisturbanceType().equals(DisturbanceType.SECTION)) {
            alterDisturbance(sectionRepository, disturbanceDTO, disturbed);
        }
        if (disturbanceDTO.getDisturbanceType().equals(DisturbanceType.STATION)) {
            alterDisturbance(stationRepository, disturbanceDTO, disturbed);
        }
    }

    /**
     * Alters disturbance of Object implementing the Disturbable Interface.
     *
     * @param repository     The matching repository for the given Element.
     * @param disturbanceDTO The DTO containing the Information about state and id of element to alter.
     * @param disturbed      The value that should be assigned to the element.
     * @param <T>            Type of element that should be altered (any Disturbable).
     * @throws MissingNodeException Gets thrown if Node could not be found.
     */
    private <T extends Disturbable> void alterDisturbance(Neo4jRepository<T, Long> repository, DisturbanceDTO disturbanceDTO, Boolean disturbed) throws MissingNodeException {
        Optional<T> elementToDisturb = repository.findById(disturbanceDTO.getIdOfDisturbedElement());
        if (elementToDisturb.isPresent()) {
            T elementToAlter = elementToDisturb.get();
            elementToAlter.setDisturbed(disturbed);
            repository.save(elementToAlter);
        } else {
            throw new MissingNodeException("Node could not be found");
        }
    }
}
