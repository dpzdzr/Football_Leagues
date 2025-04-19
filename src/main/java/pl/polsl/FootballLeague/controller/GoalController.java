package pl.polsl.FootballLeague.controller;

import java.net.URI;

import org.springframework.hateoas.CollectionModel;
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

import lombok.RequiredArgsConstructor;
import pl.polsl.FootballLeague.dto.input.GoalCreateDTO;
import pl.polsl.FootballLeague.dto.output.GoalDTO;
import pl.polsl.FootballLeague.dto.output.MatchDTO;
import pl.polsl.FootballLeague.dto.output.PlayerDTO;
import pl.polsl.FootballLeague.service.GoalService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goal")
public class GoalController {
	private final GoalService goalService;

	@GetMapping
	public CollectionModel<GoalDTO> getGoals() {
		return CollectionModel.of(goalService.getAll());
	}

	@GetMapping("/{id}")
	public GoalDTO getGoal(@PathVariable Integer id) {
		return goalService.getById(id);
	}

	@GetMapping("/{id}/match")
	public MatchDTO getMatchForGoal(@PathVariable Integer id) {
		return goalService.getMatch(id);
	}

	@GetMapping("/{id}/scorer")
	public PlayerDTO getScorerForGoal(@PathVariable Integer id) {
		return goalService.getScorer(id);
	}

	@GetMapping("/{id}/assistant")
	public PlayerDTO getAssistantForGoal(@PathVariable Integer id) {
		return goalService.getAssistant(id);
	}

	@PostMapping
	public ResponseEntity<GoalDTO> createGoal(@RequestBody GoalCreateDTO dto) {
		GoalDTO createdGoal = goalService.create(dto);
		return ResponseEntity.created(URI.create("/goals/" + createdGoal.getId())).body(createdGoal);
	}

	@PutMapping("/{id}")
	public ResponseEntity<GoalDTO> updateGoal(@PathVariable Integer id, @RequestBody GoalCreateDTO dto) {
		return ResponseEntity.ok(goalService.update(id, dto));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<GoalDTO> patchGoal(@PathVariable Integer id, @RequestBody GoalCreateDTO dto) {
		return ResponseEntity.ok(goalService.patch(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteGoal(@PathVariable Integer id) {
		goalService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
