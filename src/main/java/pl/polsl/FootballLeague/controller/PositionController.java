package pl.polsl.FootballLeague.controller;

import static pl.polsl.FootballLeague.util.DeleteUtil.detachCollection;
import static pl.polsl.FootballLeague.util.DtoMappingUtil.copyIfNotNull;
import static pl.polsl.FootballLeague.util.RepositoryUtil.existsOrThrow;
import static pl.polsl.FootballLeague.util.RepositoryUtil.findOrThrow;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
import pl.polsl.FootballLeague.repository.PlayerRepository;
import pl.polsl.FootballLeague.repository.PositionRepository;

@RestController
@RequestMapping("/position")
public class PositionController {
	@Autowired
	private PositionRepository positionRepo;
	@Autowired
	private PlayerRepository playerRepo;

	@PostMapping
	public ResponseEntity<PositionDTO> addPosition(@RequestBody Position position) {
		PositionDTO positionDTO = new PositionDTO(positionRepo.save(position));
		return ResponseEntity.created(URI.create("/position/" + positionDTO.getId())).body(positionDTO);
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
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position"));
	}

	@GetMapping("/{id}/players")
	public CollectionModel<PlayerDTO> getPlayersForPosition(@PathVariable Integer id) {
		List<PlayerDTO> playersDTO = new ArrayList<>();
		Position position = findOrThrow(positionRepo.findById(id), "Position");
		for (Player player : position.getPlayers())
			playersDTO.add(new PlayerDTO(player));
		return CollectionModel.of(playersDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PositionDTO> updatePosition(@PathVariable Integer id, @RequestBody Position updatedPosition) {
		existsOrThrow(positionRepo.existsById(id), "Position");
		updatedPosition.setId(id);
		return ResponseEntity.ok(new PositionDTO(positionRepo.save(updatedPosition)));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<PositionDTO> patchPosition(@PathVariable Integer id, @RequestBody Position patchPosition) {
		Position existingPosition = findOrThrow(positionRepo.findById(id), "Position");
		copyIfNotNull(patchPosition.getName(), existingPosition::setName);
		return ResponseEntity.ok(new PositionDTO(positionRepo.save(existingPosition)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletePositionById(@PathVariable Integer id) {
		Position existingPosition = findOrThrow(positionRepo.findById(id), "Position");
		detachCollection(existingPosition, Position::getPlayers, (p -> p.setPosition(null)), playerRepo);
		positionRepo.delete(existingPosition);
		return ResponseEntity.noContent().build();
	}

}
