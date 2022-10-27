package de.fhkiel.advancedjava.dto;

import de.fhkiel.advancedjava.enums.MeanOfTransport;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * DTO representing a TrafficLine and most of it's property.
 * NOTE: Nested Properties differ! (SectionDTO)
 */
@Setter
@Getter
@Builder
public class TrafficLineDTO implements Comparable<TrafficLineDTO>{

    private Long id;

    private Long lineId;

    private String name;

    private MeanOfTransport type;

    private List<SectionDTO> sections;

    @Override
    public int compareTo(TrafficLineDTO o) {

        return this.getId().compareTo(o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrafficLineDTO)) return false;
        TrafficLineDTO that = (TrafficLineDTO) o;
        return Objects.equals(lineId, that.lineId) &&
                Objects.equals(name, that.name) &&
                type == that.type &&
                Objects.equals(sections, that.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, name, type, sections);
    }
}
