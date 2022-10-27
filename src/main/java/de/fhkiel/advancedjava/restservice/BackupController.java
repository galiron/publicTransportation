package de.fhkiel.advancedjava.restservice;



import de.fhkiel.advancedjava.businesslogic.BackupLogic;
import de.fhkiel.advancedjava.dto.BackupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = API.BACKUP)
public class BackupController {

    private final BackupLogic backupLogic;

    @Autowired
    public BackupController(BackupLogic backupLogic) {
        this.backupLogic = backupLogic;
    }

    /**
     * Imports TrafficLine Based on a JsonFile compliant to BackupDTO.
     *
     * @param backupDTO the Format the JsonFile should be based on.
     * @return ResponseEntity with generated entities (if successful).
     */
    @PostMapping(path = API.IMPORT, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<BackupDTO> loadBackup(@RequestBody BackupDTO backupDTO) {
        try {
            return ResponseEntity.accepted().body(backupLogic.loadBackupFromJson(backupDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Creates a Backup of the Database.
     *
     * @return Backup in JsonFormat (if successful).
     */
    @GetMapping(path = API.EXPORT, produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    ResponseEntity<BackupDTO> createBackup() {
        try {
            return ResponseEntity.ok(backupLogic.createBackup());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
