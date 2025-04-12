package pl.polsl.FootballLeague.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.polsl.FootballLeague.dto.ClubDTO;
import pl.polsl.FootballLeague.dto.LeagueDTO;
import pl.polsl.FootballLeague.model.League;
import pl.polsl.FootballLeague.repository.LeagueRepository;

@RestController
@RequestMapping("/league")
public class LeagueController {
	@Autowired
	LeagueRepository leagueRepo;

	@GetMapping
	public CollectionModel<LeagueDTO> getLeagues() {
		List<LeagueDTO> leaguesDTO = StreamSupport.stream(leagueRepo.findAll().spliterator(), false).map(LeagueDTO::new)
				.toList();
		return CollectionModel.of(leaguesDTO);
	}

	@GetMapping("/{id}")
	public LeagueDTO getLeague(@PathVariable Integer id) {
		return leagueRepo.findById(id).map(LeagueDTO::new).orElseThrow(() -> new RuntimeException("League not found"));
	}

	@GetMapping("/{id}/table")
	public CollectionModel<ClubDTO> getLeagueTable(@PathVariable Integer id) {
		League league = leagueRepo.findById(id).orElseThrow(() -> new RuntimeException("League not found"));
		List<ClubDTO> clubsDTO = league.getClubs().stream()
				.sorted(Comparator.comparingInt(c -> -Optional.ofNullable(c.getPoints()).orElse(0))).map(ClubDTO::new)
				.toList();

		return CollectionModel.of(clubsDTO);
	}

	@GetMapping("/{id}/clubs")
	public CollectionModel<ClubDTO> getClubsForLeague(@PathVariable Integer id) {
		League league = leagueRepo.findById(id).orElseThrow(() -> new RuntimeException("League not found"));
		List<ClubDTO> clubsDTO = league.getClubs().stream().map(ClubDTO::new).toList();

		return CollectionModel.of(clubsDTO);
	}

	@PostMapping
	public void addLeague(@RequestBody League league) {
		leagueRepo.save(league);
	}
}
