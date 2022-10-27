package de.fhkiel.advancedjava.repository;

import de.fhkiel.advancedjava.entities.nodes.Ticket;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends Neo4jRepository<Ticket,Long> {

    List<Ticket> findAll();

    List<Ticket> findAllBySectionId(Long id);
}
