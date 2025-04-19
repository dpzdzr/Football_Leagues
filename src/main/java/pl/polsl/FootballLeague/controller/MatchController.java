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
import pl.polsl.FootballLeague.dto.input.MatchCreateDTO;
import pl.polsl.FootballLeague.dto.output.ClubDTO;
import pl.polsl.FootballLeague.dto.output.GoalDTO;
import pl.polsl.FootballLeague.dto.output.MatchDTO;
import pl.polsl.FootballLeague.service.MatchService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {
	private final MatchService matchService;

	@GetMapping
	public CollectionModel<MatchDTO> getMatches() {
		return CollectionModel.of(matchService.getAll());
	}

	@GetMapping("/{id}")
	public MatchDTO getMatch(@PathVariable Integer id) {
		return matchService.getById(id);
	}

	@GetMapping("/{id}/home_club")
	public ClubDTO getHomeClubForMatch(@PathVariable Integer id) {
		return matchService.getHomeClub(id);
	}

	@GetMapping("/{id}/away_club")
	public ClubDTO getAwayClubForMatch(@PathVariable Integer id) {
		return matchService.getAwayClub(id);
	}

	@GetMapping("/{id}/goals")
	public CollectionModel<GoalDTO> getGoalsForMatch(@PathVariable Integer id) {
		return CollectionModel.of(matchService.getGoals(id));
	}

	@PostMapping
	public ResponseEntity<MatchDTO> addMatch(@RequestBody MatchCreateDTO dto) {
		MatchDTO created = matchService.create(dto);
		return ResponseEntity.created(URI.create("/match/" + created.getId())).body(created);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<MatchDTO> updateMatch(@PathVariable Integer id, @RequestBody MatchCreateDTO dto){
		return ResponseEntity.ok(matchService.update(id, dto));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<MatchDTO> patchMatch(@PathVariable Integer id, @RequestBody MatchCreateDTO dto){
		return ResponseEntity.ok(matchService.patch(id, dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMatch(@PathVariable Integer id){
		matchService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
