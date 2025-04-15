package pl.polsl.FootballLeague.controller;

import static pl.polsl.FootballLeague.DeleteUtil.removeAssociation;
import static pl.polsl.FootballLeague.ExceptionUtil.existsOrThrow;
import static pl.polsl.FootballLeague.ExceptionUtil.findOrThrow;
import static pl.polsl.FootballLeague.PatchUtil.copyIfExists;
import static pl.polsl.FootballLeague.PatchUtil.copyIfNotNull;

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

import pl.polsl.FootballLeague.dto.GoalDTO;
import pl.polsl.FootballLeague.dto.MatchDTO;
import pl.polsl.FootballLeague.dto.PlayerDTO;
import pl.polsl.FootballLeague.model.Goal;
import pl.polsl.FootballLeague.model.Match;
import pl.polsl.FootballLeague.model.Player;
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
	public ResponseEntity<GoalDTO> addGoal(@RequestBody Goal goal) {
		GoalDTO goalDTO = new GoalDTO(goalRepo.save(goal));
		return ResponseEntity.created(URI.create("/goal/" + goalDTO.getId())).body(goalDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<GoalDTO> updateGoal(@PathVariable Integer id, @RequestBody Goal updatedGoal) {
		existsOrThrow(goalRepo.existsById(id), "Goal");
		updatedGoal.setId(id);
		GoalDTO goalDTO = new GoalDTO(goalRepo.save(updatedGoal));
		return ResponseEntity.created(URI.create("/goal/" + id)).body(goalDTO);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<GoalDTO> patchGoal(@PathVariable Integer id, @RequestBody Goal updatedGoal) {
		Goal existingGoal = findOrThrow(goalRepo.findById(id), "Goal not found");
		copyIfNotNull(updatedGoal.getMinuteScored(), existingGoal::setMinuteScored);
		copyIfExists(matchRepo, updatedGoal.getMatch(), Match::getId, existingGoal::setMatch, "Match");
		copyIfExists(playerRepo, updatedGoal.getScorer(), Player::getId, existingGoal::setScorer, "Scorer");
		copyIfExists(playerRepo, updatedGoal.getAssistant(), Player::getId, existingGoal::setAssistant, "Assistant");
		goalRepo.save(existingGoal);
		return ResponseEntity.ok(new GoalDTO(existingGoal));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteGoal(@PathVariable Integer id) {
		Goal existingGoal = findOrThrow(goalRepo.findById(id), "Goal");
		removeAssociation(matchRepo, existingGoal, Goal::getMatch, (match, g) -> match.getGoals().remove(g));
		removeAssociation(playerRepo, existingGoal, Goal::getScorer, (scorer, g) -> scorer.getGoals().remove(g));
		removeAssociation(playerRepo, existingGoal, Goal::getAssistant, (assistant, g) -> assistant.getAssists().remove(g));
		goalRepo.delete(existingGoal);
		return ResponseEntity.noContent().build();
	}
}
