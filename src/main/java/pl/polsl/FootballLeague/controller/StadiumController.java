package pl.polsl.FootballLeague.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.polsl.FootballLeague.model.Stadium;
import pl.polsl.FootballLeague.repository.StadiumRepository;

@Controller
@RequestMapping("/stadium")
public class StadiumController {
	@Autowired
	private StadiumRepository stadiumRepo;

	@GetMapping
	public @ResponseBody Iterable<Stadium> getStadiums() {
		return stadiumRepo.findAll();
	}

	@PostMapping
	public @ResponseBody void addStadium(@RequestBody Stadium stadium) {
		stadiumRepo.save(stadium);
	}
}
