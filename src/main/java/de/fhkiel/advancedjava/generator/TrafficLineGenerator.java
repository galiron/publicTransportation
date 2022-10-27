package de.fhkiel.advancedjava.generator;

import de.fhkiel.advancedjava.dto.TrafficLineDTO;
import de.fhkiel.advancedjava.entities.nodes.Section;
import de.fhkiel.advancedjava.entities.nodes.TrafficLine;
import de.fhkiel.advancedjava.enums.MeanOfTransport;

import java.util.List;

public class TrafficLineGenerator {

    /**
     * Creates a TrafficLine from TrafficLineDTO and their reflecting Sections
     *
     * @param trafficLineDto TrafficLineDto the TrafficLine is based on
     * @param sections       List of Sections that reflect those of the TrafficLineDTO
     * @return TrafficLine
     */
    public static TrafficLine createTrafficLineFromDTO(TrafficLineDTO trafficLineDto, List<Section> sections) {
        return customTrafficLine(trafficLineDto.getId()
                , trafficLineDto.getLineId()
                , trafficLineDto.getName()
                , trafficLineDto.getType()
                , sections);
    }

    /**
     * Creates a TrafficLine with custom parameters
     *
     * @param id              will be ignored, currently stopId is used for Identification
     * @param lineId          lineId
     * @param name            name
     * @param meanOfTransport meanOfTransport
     * @param sections        sections
     * @return TrafficLine
     */
    public static TrafficLine customTrafficLine(Long id, Long lineId, String name, MeanOfTransport meanOfTransport, List<Section> sections) {
        TrafficLine trafficLine = TrafficLine.builder()
//                .id(id)
                .lineId(lineId)
                .name(name)
                .meanOfTransport(meanOfTransport)
                .sections(sections)
                .build();
        trafficLine.setId(id);
        return trafficLine;
    }
}
