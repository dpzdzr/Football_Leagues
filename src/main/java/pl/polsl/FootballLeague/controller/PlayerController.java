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
import pl.polsl.FootballLeague.dto.input.PlayerCreateDTO;
import pl.polsl.FootballLeague.dto.output.ClubDTO;
import pl.polsl.FootballLeague.dto.output.GoalDTO;
import pl.polsl.FootballLeague.dto.output.PlayerDTO;
import pl.polsl.FootballLeague.dto.output.PositionDTO;
import pl.polsl.FootballLeague.service.PlayerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/player")
public class PlayerController {
	private final PlayerService playerService;

	@GetMapping
	public CollectionModel<PlayerDTO> getPlayers() {
		return CollectionModel.of(playerService.getAll());
	}

	@GetMapping("/{id}")
	public PlayerDTO getPlayer(@PathVariable Integer id) {
		return playerService.getById(id);
	}

	@GetMapping("/{id}/club")
	public ClubDTO getClubForPlayer(@PathVariable Integer id) {
		return playerService.getClub(id);
	}

	@GetMapping("/{id}/position")
	public PositionDTO getPositionForPlayer(@PathVariable Integer id) {
		return playerService.getPosition(id);
	}

	@GetMapping("/{id}/goals")
	public CollectionModel<GoalDTO> getGoalsForPlayer(@PathVariable Integer id) {
		return CollectionModel.of(playerService.getGoals(id));
	}

	@GetMapping("/{id}/assists")
	public CollectionModel<GoalDTO> getAssistsForPlayer(@PathVariable Integer id) {
		return CollectionModel.of(playerService.getAssists(id));
	}

	@PostMapping
	public ResponseEntity<PlayerDTO> addPlayer(@RequestBody PlayerCreateDTO dto) {
		PlayerDTO created = playerService.create(dto);
		return ResponseEntity.created(URI.create("/player/" + created.getId())).body(created);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Integer id, @RequestBody PlayerCreateDTO dto){ 
		return ResponseEntity.ok(playerService.update(id, dto));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<PlayerDTO> patchPlayer(@PathVariable Integer id, @RequestBody PlayerCreateDTO dto){
		return ResponseEntity.ok(playerService.patch(id, dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlayer(@PathVariable Integer id){
		playerService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
