package de.fhkiel.advancedjava.repository;


import de.fhkiel.advancedjava.entities.nodes.Section;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionRepository extends Neo4jRepository<Section,Long> {

    Optional<Section> findByStartStation_IdAndEndStation_IdAndDurationInMinutes(Long startStation_id, Long endStation_id, Integer durationInMinutes);
}
