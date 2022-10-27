package de.fhkiel.advancedjava.businesslogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fhkiel.advancedjava.dto.BackupDTO;
import de.fhkiel.advancedjava.dto.StationDTO;
import de.fhkiel.advancedjava.dto.TrafficLineDTO;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Station;
import de.fhkiel.advancedjava.entities.nodes.TrafficLine;
import de.fhkiel.advancedjava.generator.DTOGenerator;
import de.fhkiel.advancedjava.generator.StationGenerator;
import de.fhkiel.advancedjava.generator.TrafficLineGenerator;
import de.fhkiel.advancedjava.repository.*;
import lombok.AllArgsConstructor;
import org.neo4j.ogm.exception.TransactionException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

@Service
@AllArgsConstructor
public class BackupLogic {

    private final StationRepository stationRepository;
    private final TrafficLineRepository trafficLineRepository;
    private final SectionLogic sectionLogic;
    private final SectionRepository sectionRepository;
    private final VehicleRepository vehicleRepository;
    private final TicketRepository ticketRepository;

    /**
     * Logic to import a backup into the database.
     *
     * @param backupDTO Import file to load.
     * @return All created Nodes.
     */
    // returns BackupDTO for testing purposes
    public BackupDTO loadBackupFromJson(BackupDTO backupDTO) {
        trafficLineRepository.deleteAll();
        sectionRepository.deleteAll();
        stationRepository.deleteAll();
        vehicleRepository.deleteAll();
        ticketRepository.deleteAll();

        // TODO: maybe a backup if loading backup fails? Not sure if necessary because we can create manual backups before
        if ((backupDTO.getStops() != null) && (backupDTO.getTrafficLines() != null)) {
            List<Station> stationList = new ArrayList<>();
            List<TrafficLine> trafficLineList = new ArrayList<>();

            backupDTO.getStops().forEach(stationDTO -> stationList.add(StationGenerator.createStationFromDto(stationDTO)));
            Iterable<Station> stationIterable = stationRepository.saveAll(stationList);
            backupDTO.getTrafficLines().forEach(trafficLineDTO -> {
                List<Section> sections = new ArrayList<>();
                trafficLineDTO.getSections().forEach(sectionDTO -> {
                    Section section = sectionLogic.createSection(sectionDTO);
                    sections.add(section);
                });
                trafficLineList.add(TrafficLineGenerator.createTrafficLineFromDTO(trafficLineDTO, sections));
            });
            Iterable<TrafficLine> trafficLines = trafficLineRepository.saveAll(trafficLineList);
            List<TrafficLine> trafficLineToReturn = new ArrayList<>();
            trafficLines.forEach(trafficLineToReturn::add);
            return DTOGenerator.backupDTOFromStationsAndTrafficLines(stationList, trafficLineToReturn);
        } else {
            throw new IllegalArgumentException("BackupDTO is missing either stations or trafficlines");
        }
    }

    /**
     * Logic to retrieve a backup from the database.
     *
     * @return BackupDTO containing all nodes
     */
    public BackupDTO createBackup() {
        Iterable<Station> stationIterable = stationRepository.findAll();
        SortedSet<TrafficLine> trafficLineSortedSet = trafficLineRepository.findAll(2);
        List<StationDTO> stationList = new ArrayList<>();
        List<TrafficLineDTO> trafficLineList = new ArrayList<>();
        stationIterable.forEach(station -> stationList.add(DTOGenerator.stationDTOFromStation(station)));
        trafficLineSortedSet.forEach(trafficLine -> trafficLineList.add(DTOGenerator.trafficLineDTOFromTrafficLine(trafficLine)));

        return DTOGenerator.customBackupDTO(stationList, trafficLineList);
    }

    /**
     * Automated backup schedule.
     * @throws IOException if something goes wrong.
     */
    @Scheduled(cron = "${automatic.backup.cron}")
    public void automaticBackup() throws IOException {
        BackupDTO backup;
        try{
            backup = createBackup();
        }
        catch (Exception e) {
            throw new TransactionException("Something went horribly wrong while trying to make a backup!");
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("E:/fh/master/Advanced_Java/semester_repo/advancedjava2020-bannasch/src/main/resources/backup.json"), backup);
    }
}
