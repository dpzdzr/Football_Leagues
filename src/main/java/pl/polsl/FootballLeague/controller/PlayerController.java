package pl.polsl.FootballLeague.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.polsl.FootballLeague.model.Player;
import pl.polsl.FootballLeague.repository.PlayerRepository;

@Controller
@RequestMapping("/player")
public class PlayerController {
	@Autowired
	PlayerRepository playerRepo;

	@PostMapping
	public @ResponseBody void addPlayer(@RequestBody Player player) {
		player = playerRepo.save(player);
		System.out.println("Added with id=" + player.getId());
	}

	@GetMapping
	public @ResponseBody Iterable<Player> getPlayers() {
		System.out.println("Called getPlayers()");
		return playerRepo.findAll();
	}
}
