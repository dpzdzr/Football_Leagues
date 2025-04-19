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
import pl.polsl.FootballLeague.dto.input.ClubCreateDTO;
import pl.polsl.FootballLeague.dto.output.ClubDTO;
import pl.polsl.FootballLeague.dto.output.LeagueDTO;
import pl.polsl.FootballLeague.dto.output.MatchDTO;
import pl.polsl.FootballLeague.dto.output.PlayerDTO;
import pl.polsl.FootballLeague.dto.output.StadiumDTO;
import pl.polsl.FootballLeague.service.ClubService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class ClubController {
	private final ClubService clubService;

	@GetMapping
	public CollectionModel<ClubDTO> getClubs() {
		return CollectionModel.of(clubService.getAll());
	}

	@GetMapping("/{id}")
	public ClubDTO getClub(@PathVariable Integer id) {
		return clubService.getById(id);
	}

	@GetMapping("/{id}/players")
	public CollectionModel<PlayerDTO> getPlayersForClub(@PathVariable Integer id) {
		return CollectionModel.of(clubService.getPlayers(id));
	}

	@GetMapping("/{id}/league")
	public LeagueDTO getLeagueForClub(@PathVariable Integer id) {
		return clubService.getLeague(id);
	}

	@GetMapping("/{id}/home_matches")
	public CollectionModel<MatchDTO> getHomeMatchesForClub(@PathVariable Integer id) {
		return CollectionModel.of(clubService.getHomeMatches(id));
	}

	@GetMapping("/{id}/away_matches")
	public CollectionModel<MatchDTO> getAwayMatchesForClub(@PathVariable Integer id) {
		return CollectionModel.of(clubService.getAwayMatches(id));
	}

	@GetMapping("/{id}/stadium")
	public StadiumDTO getStadiumForClub(@PathVariable Integer id) {
		return clubService.getStadium(id);
	}

	@PostMapping
	public ResponseEntity<ClubDTO> addClub(@RequestBody ClubCreateDTO clubCreateDTO) {
		ClubDTO dto = clubService.create(clubCreateDTO);
		return ResponseEntity.created(URI.create("/club/" + dto.getId())).body(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClubDTO> putClub(@PathVariable Integer id, @RequestBody ClubCreateDTO dto) {
		return ResponseEntity.ok(clubService.update(id, dto));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ClubDTO> patchClub(@PathVariable Integer id, @RequestBody ClubCreateDTO dto) {
		return ResponseEntity.ok(clubService.patch(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteClub(@PathVariable Integer id) {
		clubService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
