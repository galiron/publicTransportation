package de.fhkiel.advancedjava.generator;

import de.fhkiel.advancedjava.dto.SectionDTO;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.Station;

public class SectionGenerator {

    /**
     * Generates default Section (no disturbances or connectionTime and ID)
     *
     * @param startStation StartStation of Section
     * @param endStation   EndStation of Section
     * @return Section
     */
    public static Section defaultSection(Station startStation, Station endStation) {
        return customSection(null, startStation, endStation, 0, false, 0);
    }

    /**
     * Converts DTO object with Properties to actual Section
     *
     * @param id           id
     * @param startStation startStation of Section to generate
     * @param endStation   endStation of Section to generate
     * @param sectionDTO   sectionDTO that delivers the remaining Information
     * @return Section
     */
    public static Section createSectionFromDTO(Long id, Station startStation, Station endStation, SectionDTO sectionDTO) {
        if (sectionDTO.getDurationInMinutes() == null) sectionDTO.setDurationInMinutes(0);
        if (sectionDTO.getDisturbed() == null) sectionDTO.setDisturbed(false);
        if (sectionDTO.getDisturbanceCounter() == null) sectionDTO.setDisturbanceCounter(0);
        return customSection(id, startStation, endStation, sectionDTO.getDurationInMinutes(), sectionDTO.getDisturbed(), sectionDTO.getDisturbanceCounter());
    }

    /**
     * Creates a Section with custom parameters
     *
     * @param id                 id
     * @param startStation       startStation
     * @param endStation         endStation
     * @param durationInMinutes  durationInMinutes
     * @param disturbed          disturbed
     * @param disturbanceCounter disturbanceCounter
     * @return Section
     */
    public static Section customSection(Long id, Station startStation, Station endStation, Integer durationInMinutes, Boolean disturbed, Integer disturbanceCounter) {
        Section section = Section.builder()
//                .id(id)
                .startStation(startStation)
                .endStation(endStation)
                .durationInMinutes(durationInMinutes)
                .disturbed(disturbed)
                .disturbanceCounter(disturbanceCounter)
                .build();
        section.setId(id);
        return section;
    }
}
