package pl.polsl.FootballLeague.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import pl.polsl.FootballLeague.dto.MatchDTO;
import pl.polsl.FootballLeague.dto.PlayerDTO;
import pl.polsl.FootballLeague.model.Goal;
import pl.polsl.FootballLeague.repository.GoalRepository;

@RestController
@RequestMapping("/goal")
public class GoalController {
	@Autowired
	private GoalRepository goalRepo;

	@GetMapping
	public CollectionModel<GoalDTO> getGoals() {
		List<GoalDTO> goalsDTO = new ArrayList<>();
		for (Goal goal : goalRepo.findAll())
			goalsDTO.add(new GoalDTO(goal));
		return CollectionModel.of(goalsDTO);
	}

	@GetMapping("/{id}")
	public GoalDTO getGoal(@PathVariable Integer id) {
		return goalRepo.findById(id).map(GoalDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));
	}

	@GetMapping("/{id}/match")
	public MatchDTO getMatchForGoal(@PathVariable Integer id) {
		Goal goal = goalRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

		return Optional.ofNullable(goal.getMatch()).map(MatchDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not assigned"));
	}

	@GetMapping("/{id}/scorer")
	public PlayerDTO getScorerForGoal(@PathVariable Integer id) {
		Goal goal = goalRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));
		return Optional.ofNullable(goal.getScorer()).map(PlayerDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scorer not assigned"));
	}

	@GetMapping("/{id}/assistant")
	public PlayerDTO getAssistantForGoal(@PathVariable Integer id) {
		Goal goal = goalRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

		return Optional.ofNullable(goal.getAssistant()).map(PlayerDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assistant not assigned"));
	}

	@PostMapping
	public void addGoal(@RequestBody Goal goal) {
		goalRepo.save(goal);
	}
}
