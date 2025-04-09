package pl.polsl.FootballLeague.repository;

import org.springframework.data.repository.CrudRepository;

import pl.polsl.FootballLeague.model.Match;

public interface MatchRepository extends CrudRepository<Match, Integer> {

}
