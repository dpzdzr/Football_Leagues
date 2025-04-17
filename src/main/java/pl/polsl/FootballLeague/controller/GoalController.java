package pl.polsl.FootballLeague.controller;

import static pl.polsl.FootballLeague.util.DeleteUtil.detachSingle;
import static pl.polsl.FootballLeague.util.RepositoryUtil.findOrThrow;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pl.polsl.FootballLeague.dto.GoalCreateDTO;
import pl.polsl.FootballLeague.dto.GoalDTO;
import pl.polsl.FootballLeague.dto.MatchDTO;
import pl.polsl.FootballLeague.dto.PlayerDTO;
import pl.polsl.FootballLeague.mapper.GoalMapper;
import pl.polsl.FootballLeague.model.Goal;
import pl.polsl.FootballLeague.repository.GoalRepository;
import pl.polsl.FootballLeague.repository.MatchRepository;
import pl.polsl.FootballLeague.repository.PlayerRepository;

@RestController
@RequestMapping("/goal")
public class GoalController {
	@Autowired
	private GoalRepository goalRepo;
	@Autowired
	private MatchRepository matchRepo;
	@Autowired
	private PlayerRepository playerRepo;

	@GetMapping
	public CollectionModel<GoalDTO> getGoals() {
		List<GoalDTO> goalsDTO = new ArrayList<>();
		for (Goal goal : goalRepo.findAll())
			goalsDTO.add(new GoalDTO(goal));
		return CollectionModel.of(goalsDTO);
	}

	@GetMapping("/{id}")
	public GoalDTO getGoal(@PathVariable Integer id) {
		return new GoalDTO(findOrThrow(goalRepo.findById(id), "Goal"));
	}

	@GetMapping("/{id}/match")
	public MatchDTO getMatchForGoal(@PathVariable Integer id) {
		Goal goal = findOrThrow(goalRepo.findById(id), "Goal");
		return Optional.ofNullable(goal.getMatch()).map(MatchDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not assigned"));
	}

	@GetMapping("/{id}/scorer")
	public PlayerDTO getScorerForGoal(@PathVariable Integer id) {
		Goal goal = findOrThrow(goalRepo.findById(id), "Goal");
		return Optional.ofNullable(goal.getScorer()).map(PlayerDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scorer not assigned"));
	}

	@GetMapping("/{id}/assistant")
	public PlayerDTO getAssistantForGoal(@PathVariable Integer id) {
		Goal goal = findOrThrow(goalRepo.findById(id), "Goal");
		return Optional.ofNullable(goal.getAssistant()).map(PlayerDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assistant not assigned"));
	}

	@PostMapping
	public ResponseEntity<GoalDTO> postGoal(@RequestBody GoalCreateDTO postGoal) {
		Goal newGoal = new Goal();
		GoalMapper.toEntity(postGoal, newGoal, matchRepo, playerRepo);
		GoalDTO goalDTO = new GoalDTO(goalRepo.save(newGoal));
		return ResponseEntity.created(URI.create("/goal/" + goalDTO.getId())).body(goalDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<GoalDTO> putGoal(@PathVariable Integer id, @RequestBody GoalCreateDTO putGoal) {
		Goal existingGoal = findOrThrow(goalRepo.findById(id), "Goal");
		GoalMapper.put(putGoal, existingGoal, goalRepo, matchRepo, playerRepo);
		GoalDTO goalDTO = new GoalDTO(goalRepo.save(existingGoal));
		return ResponseEntity.created(URI.create("/goal/" + id)).body(goalDTO);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<GoalDTO> patchGoal(@PathVariable Integer id, @RequestBody GoalCreateDTO patchGoal) {
		Goal existingGoal = findOrThrow(goalRepo.findById(id), "Goal not found");
		GoalMapper.patch(patchGoal, existingGoal, goalRepo, matchRepo, playerRepo);
		goalRepo.save(existingGoal);
		return ResponseEntity.ok(new GoalDTO(existingGoal));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteGoal(@PathVariable Integer id) {
		Goal existingGoal = findOrThrow(goalRepo.findById(id), "Goal");
		detachSingle(existingGoal, Goal::getMatch, (match, g) -> match.getGoals().remove(g), matchRepo);
		detachSingle(existingGoal, Goal::getScorer, (scorer, g) -> scorer.getGoals().remove(g), playerRepo);
		detachSingle(existingGoal, Goal::getAssistant, (assistant, g) -> assistant.getAssists().remove(g), playerRepo);
		existingGoal.setMatch(null);
		existingGoal.setScorer(null);
		existingGoal.setAssistant(null);
		goalRepo.delete(existingGoal);
		return ResponseEntity.noContent().build();
	}
}
