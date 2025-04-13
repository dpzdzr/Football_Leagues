package pl.polsl.FootballLeague.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import pl.polsl.FootballLeague.dto.PlayerDTO;
import pl.polsl.FootballLeague.dto.PositionDTO;
import pl.polsl.FootballLeague.model.Player;
import pl.polsl.FootballLeague.model.Position;
import pl.polsl.FootballLeague.repository.PositionRepository;

@RestController
@RequestMapping("/position")
public class PositionController {
	@Autowired
	private PositionRepository positionRepo;

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
	public PositionDTO getPosition(@PathVariable Integer id) {
		return positionRepo.findById(id).map(PositionDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
	}

	@GetMapping("/{id}/players")
	public CollectionModel<PlayerDTO> getPlayersForPosition(@PathVariable Integer id) {
		List<PlayerDTO> playersDTO = new ArrayList<>();
		Position position = positionRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
		for (Player player : position.getPlayers())
			playersDTO.add(new PlayerDTO(player));

		return CollectionModel.of(playersDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePosition(@PathVariable Integer id, @RequestBody Position updatedPosition) {
//		Position existingPosition = positionRepo.findById(id)
//		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
//
//		existingPosition.setName(updatedPosition.getName());
//		return new PositionDTO(positionRepo.save(existingPosition));

		if (!positionRepo.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Position not found");
		}

		updatedPosition.setId(id);
		return ResponseEntity.ok(new PositionDTO(positionRepo.save(updatedPosition)));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> patchPosition(@PathVariable Integer id, @RequestBody Position patchPosition) {
		Optional<Position> optionalPosition = positionRepo.findById(id);
		if (optionalPosition.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Position not found");
		}

		Position existingPosition = optionalPosition.get();
		if (patchPosition.getName() != null)
			existingPosition.setName(patchPosition.getName());
		return ResponseEntity.ok(new PositionDTO(positionRepo.save(existingPosition)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePositionById(@PathVariable Integer id) {
		if (!positionRepo.findById(id).isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Position not found");
		}

		positionRepo.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
