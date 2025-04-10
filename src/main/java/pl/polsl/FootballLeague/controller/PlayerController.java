package pl.polsl.FootballLeague.controller;

import java.util.ArrayList;
import java.util.List;

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

import pl.polsl.FootballLeague.dto.PlayerDTO;
import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.Player;
import pl.polsl.FootballLeague.repository.PlayerRepository;

@RestController
@RequestMapping("/player")
public class PlayerController {
	@Autowired
	PlayerRepository playerRepo;

	@PostMapping
	public void addPlayer(@RequestBody Player player) {
		player = playerRepo.save(player);
	}

	@GetMapping
	public CollectionModel<PlayerDTO> getPlayers() {
		List<PlayerDTO> playersDTO = new ArrayList<>();
		for (Player player : playerRepo.findAll())
			playersDTO.add(new PlayerDTO(player));
		return CollectionModel.of(playersDTO);

	}

	@GetMapping("/{id}/club")
	public Club getClubForPlayer(@PathVariable Integer id) {
		return playerRepo.findById(id).map(Player::getClub)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
	}

}
