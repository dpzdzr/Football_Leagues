package pl.polsl.FootballLeague.controller;

import static pl.polsl.FootballLeague.util.DtoMappingUtil.copyIfNotNull;
import static pl.polsl.FootballLeague.util.RepositoryUtil.existsOrThrow;
import static pl.polsl.FootballLeague.util.RepositoryUtil.findOrThrow;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
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

import pl.polsl.FootballLeague.dto.ClubDTO;
import pl.polsl.FootballLeague.dto.LeagueDTO;
import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.League;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.LeagueRepository;

@RestController
@RequestMapping("/league")
public class LeagueController {
	@Autowired
	LeagueRepository leagueRepo;
	@Autowired
	ClubRepository clubRepo;

	@GetMapping
	public CollectionModel<LeagueDTO> getLeagues() {
		List<LeagueDTO> leaguesDTO = StreamSupport.stream(leagueRepo.findAll().spliterator(), false).map(LeagueDTO::new)
				.toList();
		return CollectionModel.of(leaguesDTO);
	}

	@GetMapping("/{id}")
	public LeagueDTO getLeague(@PathVariable Integer id) {
		return new LeagueDTO(findOrThrow(leagueRepo.findById(id), "League"));
	}

	@GetMapping("/{id}/table")
	public CollectionModel<ClubDTO> getLeagueTable(@PathVariable Integer id) {
		League league = findOrThrow(leagueRepo.findById(id), "League");
		List<ClubDTO> clubsDTO = league.getClubs().stream()
				.sorted(Comparator.comparingInt(c -> -Optional.ofNullable(c.getPoints()).orElse(0))).map(ClubDTO::new)
				.toList();

		return CollectionModel.of(clubsDTO);
	}

	@GetMapping("/{id}/clubs")
	public CollectionModel<ClubDTO> getClubsForLeague(@PathVariable Integer id) {
		League league = findOrThrow(leagueRepo.findById(id), "League");
		List<ClubDTO> clubsDTO = league.getClubs().stream().map(ClubDTO::new).toList();
		return CollectionModel.of(clubsDTO);
	}

	@PostMapping
	public ResponseEntity<LeagueDTO> addLeague(@RequestBody League league) {
		LeagueDTO leagueDTO = new LeagueDTO(leagueRepo.save(league));
		return ResponseEntity.created(URI.create("/league" + leagueDTO.getId())).body(leagueDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<LeagueDTO> putLeague(@PathVariable Integer id, @RequestBody League putLeague) {
		existsOrThrow(leagueRepo.existsById(id), "League");
		putLeague.setId(id);
		return ResponseEntity.ok(new LeagueDTO(leagueRepo.save(putLeague)));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<LeagueDTO> patchLeague(@PathVariable Integer id, @RequestBody League patchLeague) {
		League existingLeague = findOrThrow(leagueRepo.findById(id), "League");
		copyIfNotNull(patchLeague.getName(), existingLeague::setName);
		copyIfNotNull(patchLeague.getCountry(), existingLeague::setCountry);
		return ResponseEntity.ok(new LeagueDTO(leagueRepo.save(existingLeague)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteLeagueById(@PathVariable Integer id) {
		League league = findOrThrow(leagueRepo.findById(id), "League");
		clubRepo.saveAll(detachClubs(league));
		leagueRepo.delete(league);
		return ResponseEntity.noContent().build();
	}

	private List<Club> detachClubs(League league) {
		return league.getClubs().stream().peek(c -> c.setLeague(null)).toList();
	}
}
