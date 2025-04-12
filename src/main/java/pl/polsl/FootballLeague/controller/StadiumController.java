package pl.polsl.FootballLeague.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

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

import pl.polsl.FootballLeague.dto.ClubDTO;
import pl.polsl.FootballLeague.dto.StadiumDTO;
import pl.polsl.FootballLeague.model.Stadium;
import pl.polsl.FootballLeague.repository.StadiumRepository;

@RestController
@RequestMapping("/stadium")
public class StadiumController {
	@Autowired
	private StadiumRepository stadiumRepo;

	@GetMapping
	public CollectionModel<StadiumDTO> getStadiums() {
		List<StadiumDTO> stadiumDTOs = StreamSupport.stream(stadiumRepo.findAll().spliterator(), false)
				.map(StadiumDTO::new).toList();

		return CollectionModel.of(stadiumDTOs);
	}

	@GetMapping("/{id}")
	public StadiumDTO getStadium(@PathVariable Integer id) {
		Stadium stadium = stadiumRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stadium not found"));

		return new StadiumDTO(stadium);
	}

	@GetMapping("/{id}/club")
	public ClubDTO getClubForStadium(@PathVariable Integer id) {
		Stadium stadium = stadiumRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stadium not found"));
		return Optional.ofNullable(stadium.getClub()).map(ClubDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not assigned"));
	}

	@PostMapping
	public void addStadium(@RequestBody Stadium stadium) {
		stadiumRepo.save(stadium);
	}
}
