package pl.polsl.FootballLeague.controller;

import java.net.URI;

import org.springframework.hateoas.CollectionModel;
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

import lombok.RequiredArgsConstructor;
import pl.polsl.FootballLeague.dto.input.PositionCreateDTO;
import pl.polsl.FootballLeague.dto.output.PlayerDTO;
import pl.polsl.FootballLeague.dto.output.PositionDTO;
import pl.polsl.FootballLeague.service.PositionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/position")
public class PositionController {
	private final PositionService positionService;

	@GetMapping
	public CollectionModel<PositionDTO> getPositions() {
		return CollectionModel.of(positionService.getAll());
	}

	@GetMapping("/{id}")
	public PositionDTO getPosition(@PathVariable Integer id) {
		return positionService.getById(id);
	}

	@GetMapping("/{id}/players")
	public CollectionModel<PlayerDTO> getPlayersForPosition(@PathVariable Integer id) {
		return CollectionModel.of(positionService.getPlayers(id));
	}

	@PostMapping
	public ResponseEntity<PositionDTO> addPosition(@RequestBody PositionCreateDTO dto) {
		PositionDTO created = positionService.create(dto);
		return ResponseEntity.created(URI.create("/position/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PositionDTO> updatePosition(@PathVariable Integer id, @RequestBody PositionCreateDTO dto) {
		return ResponseEntity.ok(positionService.update(id, dto));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<PositionDTO> patchPosition(@PathVariable Integer id, @RequestBody PositionCreateDTO dto) {
		return ResponseEntity.ok(positionService.patch(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletePositionById(@PathVariable Integer id) {
		positionService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
