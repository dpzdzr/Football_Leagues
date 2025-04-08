package pl.polsl.FootballLeague.repository;

import org.springframework.data.repository.CrudRepository;

import pl.polsl.FootballLeague.model.Player;

public interface PlayerRepository extends CrudRepository<Player, Integer> {
}
