package pl.polsl.FootballLeague.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pl.polsl.FootballLeague.dto.PositionDTO;
import pl.polsl.FootballLeague.model.Player;
import pl.polsl.FootballLeague.model.Position;
import pl.polsl.FootballLeague.repository.PositionRepository;

@RestController
@RequestMapping("/position")
public class PositionController {
	@Autowired
	PositionRepository positionRepo;

	@PostMapping
	public void addPosition(@RequestBody Position position) {
		positionRepo.save(position);
	}

	@GetMapping
	public CollectionModel<PositionDTO> getPositions() {
		List<PositionDTO> positionsDTO = new ArrayList<>();
		for (Position position : positionRepo.findAll())
			positionsDTO.add(new PositionDTO(position));
		return CollectionModel.of(positionsDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PositionDTO> getPosition(@PathVariable Integer id) {
		return positionRepo.findById(id).map(PositionDTO::new).map(ResponseEntity::ok)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
	}

	@GetMapping("/{id}/players")
	public ResponseEntity<List<Player>> getPlayersByPosition(@PathVariable Integer id) {
		return positionRepo.findById(id).map(Position::getPlayers).map(ResponseEntity::ok)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePosition(@PathVariable Integer id, @RequestBody Position updatedPosition) {
		Position existingPosition = positionRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));

		existingPosition.setName(updatedPosition.getName());
		return ResponseEntity.ok(new PositionDTO(positionRepo.save(existingPosition)));

	}

}
