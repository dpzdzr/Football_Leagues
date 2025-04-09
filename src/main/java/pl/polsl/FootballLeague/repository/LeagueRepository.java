package pl.polsl.FootballLeague.repository;

import org.springframework.data.repository.CrudRepository;

import pl.polsl.FootballLeague.model.League;

public interface LeagueRepository extends CrudRepository<League, Integer> {

}
