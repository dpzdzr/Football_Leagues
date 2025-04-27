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
import pl.polsl.FootballLeague.dto.input.StadiumCreateDTO;
import pl.polsl.FootballLeague.dto.output.ClubDTO;
import pl.polsl.FootballLeague.dto.output.StadiumDTO;
import pl.polsl.FootballLeague.service.StadiumService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stadium")
public class StadiumController {
	private final StadiumService stadiumService;

	@GetMapping
	public CollectionModel<StadiumDTO> getStadiums() {
		return CollectionModel.of(stadiumService.getAll());
	}

	@GetMapping("/{id}")
	public StadiumDTO getStadium(@PathVariable Integer id) {
		return stadiumService.getById(id);
	}

	@GetMapping("/{id}/clubs")
	public CollectionModel<ClubDTO> getClubsForStadium(@PathVariable Integer id) {
		return CollectionModel.of(stadiumService.getClubs(id));
	}

	@PostMapping
	public ResponseEntity<StadiumDTO> addStadium(@RequestBody StadiumCreateDTO dto) {
		StadiumDTO created = stadiumService.create(dto);
		return ResponseEntity.created(URI.create("/stadium/" + created.getId())).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StadiumDTO> updateStadium(@PathVariable Integer id, @RequestBody StadiumCreateDTO dto) {
		return ResponseEntity.ok(stadiumService.update(id, dto));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<StadiumDTO> patchStadium(@PathVariable Integer id, @RequestBody StadiumCreateDTO dto) {
		return ResponseEntity.ok(stadiumService.patch(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStadium(@PathVariable Integer id) {
		stadiumService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
