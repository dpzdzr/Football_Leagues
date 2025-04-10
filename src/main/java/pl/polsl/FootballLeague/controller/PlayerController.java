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

import pl.polsl.FootballLeague.dto.GoalDTO;
import pl.polsl.FootballLeague.dto.PlayerDTO;
import pl.polsl.FootballLeague.dto.PositionDTO;
import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.Goal;
import pl.polsl.FootballLeague.model.Player;
import pl.polsl.FootballLeague.model.Position;
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
		Player player = playerRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));

		Club club = player.getClub();
		if (club == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not assigned");

		return club;
	}

	@GetMapping("/{id}/position")
	public PositionDTO getPositionForPlayer(@PathVariable Integer id) {
		Player player = playerRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));

		Position position = player.getPosition();
		if (position == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not assigned");

		return (new PositionDTO(position));
	}

	@GetMapping("/{id}/goals")
	public CollectionModel<GoalDTO> getGoalsForPlayer(@PathVariable Integer id) {
		Player player = playerRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
		List<GoalDTO> goalsDTO = new ArrayList<>();
		for (Goal goal : player.getGoals())
			goalsDTO.add(new GoalDTO(goal));

		return CollectionModel.of(goalsDTO);
	}

}
