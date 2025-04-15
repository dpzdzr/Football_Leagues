package pl.polsl.FootballLeague.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

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

import pl.polsl.FootballLeague.dto.ClubDTO;
import pl.polsl.FootballLeague.dto.GoalDTO;
import pl.polsl.FootballLeague.dto.MatchDTO;
import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.Match;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.MatchRepository;

@RestController
@RequestMapping("/match")
public class MatchController {
	@Autowired
	private MatchRepository matchRepo;
	@Autowired
	private ClubRepository clubRepo;

	@GetMapping
	public CollectionModel<MatchDTO> getMatches() {
		List<MatchDTO> matchDTOs = StreamSupport.stream(matchRepo.findAll().spliterator(), false).map(MatchDTO::new)
				.toList();
		return CollectionModel.of(matchDTOs);
	}

	@GetMapping("/{id}")
	public MatchDTO getMatch(@PathVariable Integer id) {
		return matchRepo.findById(id).map(MatchDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));
	}

	@GetMapping("/{id}/home_club")
	public ClubDTO getHomeClubForMatch(@PathVariable Integer id) {
		Match match = findMatchOrThrowException(id);
		return new ClubDTO(Optional.ofNullable(match.getHomeClub())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Home club not assigned")));
	}

	@GetMapping("/{id}/away_club")
	public ClubDTO getAwayClubForMatch(@PathVariable Integer id) {
		Match match = findMatchOrThrowException(id);
		return new ClubDTO(Optional.ofNullable(match.getAwayClub())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Away club not assigned")));
	}

	@GetMapping("/{id}/goals")
	public CollectionModel<GoalDTO> getGoalsForMatch(@PathVariable Integer id) {
		List<GoalDTO> goalDTOs = StreamSupport.stream(findMatchOrThrowException(id).getGoals().spliterator(), false)
				.map(GoalDTO::new).toList();
		return CollectionModel.of(goalDTOs);
	}

	@PostMapping
	public void addMatch(@RequestBody Match match) {
//		Club home = clubRepo.findById(match.getHomeClub().getId())
//				.orElseThrow(() -> new RuntimeException("Home club not found"));
//		Club away = clubRepo.findById(match.getAwayClub().getId())
//				.orElseThrow(() -> new RuntimeException("Away club not found"));
//		match.setHomeClub(home);
//		match.setAwayClub(away);

		match = matchRepo.save(match);

		this.addPointsByResult(match);
	}

	private Match findMatchOrThrowException(Integer id) {
		return matchRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found"));
	}

	private void addPointsByResult(Match match) {
		if (match.getHomeScore() == null || match.getAwayScore() == null)
			return;
		
		if (match.getHomeScore() > match.getAwayScore()) {
			match.getHomeClub().addWin();
		} else if (match.getHomeScore() < match.getAwayScore()) {
			match.getAwayClub().addWin();
		} else {
			match.getHomeClub().addDraw();
			match.getAwayClub().addDraw();
		}
		clubRepo.save(match.getHomeClub());
		clubRepo.save(match.getAwayClub());
	}
}
