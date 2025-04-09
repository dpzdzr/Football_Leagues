package pl.polsl.FootballLeague.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.Match;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.MatchRepository;

@Controller
@RequestMapping("/match")
public class MatchController {
	@Autowired
	private MatchRepository matchRepo;
	@Autowired
	private ClubRepository clubRepo;

	@GetMapping
	public @ResponseBody Iterable<Match> getMatches() {
		return matchRepo.findAll();
	}

	@PostMapping
	public @ResponseBody void addMatch(@RequestBody Match match) {
		Club home = clubRepo.findById(match.getHomeClub().getId())
				.orElseThrow(() -> new RuntimeException("Home club not found"));
		Club away = clubRepo.findById(match.getAwayClub().getId())
				.orElseThrow(() -> new RuntimeException("Away club not found"));
		match.setHomeClub(home);
		match.setAwayClub(away);
		
		match = matchRepo.save(match);
		this.addPointsByResult(match);	
	}

	private void addPointsByResult(Match match) {
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
