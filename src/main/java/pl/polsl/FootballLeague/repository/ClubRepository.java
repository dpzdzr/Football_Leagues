package pl.polsl.FootballLeague.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.League;

public interface ClubRepository extends CrudRepository<Club, Integer> {
	List<Club> findByLeagueOrderByPointsDesc(League league);
}
