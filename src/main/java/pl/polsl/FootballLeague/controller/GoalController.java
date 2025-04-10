package pl.polsl.FootballLeague.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pl.polsl.FootballLeague.dto.GoalDTO;
import pl.polsl.FootballLeague.dto.PlayerDTO;
import pl.polsl.FootballLeague.model.Goal;
import pl.polsl.FootballLeague.repository.GoalRepository;

@RestController
@RequestMapping("/goal")
public class GoalController {
	@Autowired
	GoalRepository goalRepo;

//	@GetMapping
//	public Iterable<Goal> getGoals() {
//		return goalRepo.findAll();
//	}

	@GetMapping
	public CollectionModel<GoalDTO> getGoals() {
		List<GoalDTO> goalsDTO = new ArrayList<>();
		for (Goal goal : goalRepo.findAll())
			goalsDTO.add(new GoalDTO(goal));
		return CollectionModel.of(goalsDTO);
	}

	@GetMapping("{id}/scorer")
	public PlayerDTO getScorer(@PathVariable Integer id)
	{
		return goalRepo.findById(id).map(Goal::getScorer).map(PlayerDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));
	}
	
	@GetMapping("{id}/assistant")
	public PlayerDTO getAssistant(@PathVariable Integer id)
	{
		return goalRepo.findById(id).map(Goal::getAssistant).map(PlayerDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));
	}

	@PostMapping
	public void addGoal(@RequestBody Goal goal) {
		goalRepo.save(goal);
	}
}
