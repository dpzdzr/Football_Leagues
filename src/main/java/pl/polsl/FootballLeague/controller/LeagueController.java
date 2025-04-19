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
import pl.polsl.FootballLeague.dto.input.LeagueCreateDTO;
import pl.polsl.FootballLeague.dto.output.ClubDTO;
import pl.polsl.FootballLeague.dto.output.LeagueDTO;
import pl.polsl.FootballLeague.service.LeagueService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/league")
public class LeagueController {
	private final LeagueService leagueService;
	
	@GetMapping
	public CollectionModel<LeagueDTO> getLeagues() {
		return CollectionModel.of(leagueService.getAll());
	}

	@GetMapping("/{id}")
	public LeagueDTO getLeague(@PathVariable Integer id) {
		return leagueService.getById(id);
	}

	@GetMapping("/{id}/table")
	public CollectionModel<ClubDTO> getLeagueTable(@PathVariable Integer id) {
		return CollectionModel.of(leagueService.getTable(id));
	}

	@GetMapping("/{id}/clubs")
	public CollectionModel<ClubDTO> getClubsForLeague(@PathVariable Integer id) {
		return CollectionModel.of(leagueService.getClubs(id));
	}

	@PostMapping
	public ResponseEntity<LeagueDTO> addLeague(@RequestBody LeagueCreateDTO dto) {
		LeagueDTO created = leagueService.create(dto);
		return ResponseEntity.created(URI.create("/league/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<LeagueDTO> putLeague(@PathVariable Integer id, @RequestBody LeagueCreateDTO dto) {
		return ResponseEntity.ok(leagueService.update(id, dto));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<LeagueDTO> patchLeague(@PathVariable Integer id, @RequestBody LeagueCreateDTO dto) {
		return ResponseEntity.ok(leagueService.patch(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteLeagueById(@PathVariable Integer id) {
		leagueService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
