package de.fhkiel.advancedjava.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * DTO representing a Section with most of it's properties
 * NOTE: Instead of stations, the DTO only holds the reference to those Stations!
 */
@Getter
@Setter
@Builder
public class SectionDTO implements Comparable<SectionDTO> {

    private Long sectionId;

    private Long beginStopId;

    private Long endStopId;

    @Builder.Default
    private Integer durationInMinutes = 0;

    @Builder.Default
    private Boolean disturbed = false;

    @Builder.Default
    private Integer disturbanceCounter = 0;

    @Override
    public int compareTo(SectionDTO o) {
        return this.getBeginStopId().compareTo(o.getBeginStopId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SectionDTO)) return false;
        SectionDTO that = (SectionDTO) o;
        return Objects.equals(beginStopId, that.beginStopId) &&
                Objects.equals(endStopId, that.endStopId) &&
                Objects.equals(durationInMinutes, that.durationInMinutes) &&
                Objects.equals(disturbed, that.disturbed) &&
                Objects.equals(disturbanceCounter, that.disturbanceCounter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginStopId, endStopId, durationInMinutes, disturbed, disturbanceCounter);
    }
}
