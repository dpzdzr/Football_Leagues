package pl.polsl.FootballLeague.repository;

import org.springframework.data.repository.CrudRepository;

import pl.polsl.FootballLeague.model.Position;

public interface PositionRepository extends CrudRepository<Position, Integer> {

}
