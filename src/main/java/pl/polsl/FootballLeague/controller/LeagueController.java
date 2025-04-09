package pl.polsl.FootballLeague.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.League;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.LeagueRepository;

@Controller
@RequestMapping("/league")
public class LeagueController {
	@Autowired
	LeagueRepository leagueRepo;
	@Autowired
	ClubRepository clubRepo;

	@GetMapping
	public @ResponseBody Iterable<League> getLeagues() {
		return leagueRepo.findAll();
	}

	@GetMapping("/{leagueId}/table")
	public @ResponseBody List<Club> getLeagueTable(@PathVariable Integer leagueId) {
		League league = leagueRepo.findById(leagueId)
				.orElseThrow(()-> new RuntimeException("League not found"));
		return clubRepo.findByLeagueOrderByPointsDesc(league);
	}

	@PostMapping
	public @ResponseBody void addLeague(@RequestBody League league) {
		leagueRepo.save(league);
	}
}
