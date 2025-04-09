package pl.polsl.FootballLeague.repository;

import org.springframework.data.repository.CrudRepository;

import pl.polsl.FootballLeague.model.Goal;

public interface GoalRepository extends CrudRepository<Goal, Integer>{

}
