package pl.polsl.FootballLeague.controller;

import static pl.polsl.FootballLeague.util.DtoMappingUtil.copyIfNotNull;
import static pl.polsl.FootballLeague.util.RepositoryUtil.existsOrThrow;
import static pl.polsl.FootballLeague.util.RepositoryUtil.findOrThrow;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

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

import pl.polsl.FootballLeague.dto.ClubDTO;
import pl.polsl.FootballLeague.dto.StadiumDTO;
import pl.polsl.FootballLeague.model.Stadium;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.StadiumRepository;

@RestController
@RequestMapping("/stadium")
public class StadiumController {
	@Autowired
	private StadiumRepository stadiumRepo;
	@Autowired
	private ClubRepository clubRepo;

	@GetMapping
	public CollectionModel<StadiumDTO> getStadiums() {
		List<StadiumDTO> stadiumDTOs = StreamSupport.stream(stadiumRepo.findAll().spliterator(), false)
				.map(StadiumDTO::new).toList();

		return CollectionModel.of(stadiumDTOs);
	}

	@GetMapping("/{id}")
	public StadiumDTO getStadium(@PathVariable Integer id) {
		Stadium stadium = findOrThrow(stadiumRepo.findById(id), "Stadium");
		return new StadiumDTO(stadium);
	}

	@GetMapping("/{id}/club")
	public ClubDTO getClubForStadium(@PathVariable Integer id) {
		Stadium stadium = findOrThrow(stadiumRepo.findById(id), "Stadium");
		return Optional.ofNullable(stadium.getClub()).map(ClubDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not assigned"));
	}

	@PostMapping
	public ResponseEntity<StadiumDTO> addStadium(@RequestBody Stadium stadium) {
		StadiumDTO stadiumDTO = new StadiumDTO(stadiumRepo.save(stadium));
		return ResponseEntity.created(URI.create("/stadium/" + stadiumDTO.getId())).body(stadiumDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StadiumDTO> updateStadium(@PathVariable Integer id, @RequestBody Stadium updatedStadium) {
		existsOrThrow(stadiumRepo.existsById(id), "Stadium");
		updatedStadium.setId(id);
		return ResponseEntity.ok(new StadiumDTO(stadiumRepo.save(updatedStadium)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteStadium(@PathVariable Integer id) {
		Stadium existingStadium = findOrThrow(stadiumRepo.findById(id), "Stadium");

		Optional.ofNullable(existingStadium.getClub()).ifPresent(c -> {
			c.setStadium(null);
			clubRepo.save(c);
		});

		stadiumRepo.delete(existingStadium);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> patchStadium(@PathVariable Integer id, @RequestBody Stadium patchStadium) {
		Stadium existingStadium = findOrThrow(stadiumRepo.findById(id), "Stadium");
		updatedNotNullFields(patchStadium, existingStadium);

		return ResponseEntity.ok(new StadiumDTO(stadiumRepo.save(existingStadium)));
	}

	private void updatedNotNullFields(Stadium source, Stadium target) {
		copyIfNotNull(source.getName(), target::setName);
		copyIfNotNull(source.getCapacity(), target::setCapacity);
		copyIfNotNull(source.getAddress(), target::setAddress);
	}
}
