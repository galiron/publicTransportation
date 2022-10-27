package de.fhkiel.advancedjava.repository;

import de.fhkiel.advancedjava.entities.nodes.TrafficLine;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.SortedSet;

@Repository
public interface TrafficLineRepository extends Neo4jRepository<TrafficLine,Long> {

    SortedSet<TrafficLine> findAll(int depth);
}
