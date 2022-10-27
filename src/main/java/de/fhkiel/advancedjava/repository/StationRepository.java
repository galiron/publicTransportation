package de.fhkiel.advancedjava.repository;

import de.fhkiel.advancedjava.entities.nodes.Station;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.SortedSet;

@Repository
public interface StationRepository extends Neo4jRepository<Station,Long> {

    SortedSet<Station> findAll ();

    Optional<Station> findStationByStopId(Long id);

    SortedSet<Station> findByName(String name);

    SortedSet<Station> findByStopId(Long stopId);
}
