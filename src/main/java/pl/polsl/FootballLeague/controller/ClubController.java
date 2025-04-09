package pl.polsl.FootballLeague.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.repository.ClubRepository;

@Controller
@RequestMapping("/club")
public class ClubController {
	@Autowired
	ClubRepository clubRepo;

	@GetMapping
	public @ResponseBody Iterable<Club> getClubs() {
		return clubRepo.findAll();
	}

	@PutMapping("/{id}")
	public @ResponseBody ResponseEntity<?> updateClub(@PathVariable Integer id, @RequestBody Club updatedClub) {
		Optional<Club> optionalClub = clubRepo.findById(id);
		if (optionalClub.isPresent()) {
			Club existingClub = optionalClub.get();
			updateNonNullableFields(updatedClub, existingClub);
			
			clubRepo.save(existingClub);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public @ResponseBody void addClub(@RequestBody Club club) {
		clubRepo.save(club);
	}

	@PostMapping("/batch")
	public @ResponseBody void addClubs(@RequestBody List<Club> clubs) {
		clubRepo.saveAll(clubs);
	}

	private void updateNonNullableFields(Club source, Club target) {
		if (source.getName() != null)
			target.setName(source.getName());
		if (source.getCity() != null)
			target.setCity(source.getCity());
		if (source.getFoundationYear() != null)
			target.setFoundationYear(source.getFoundationYear());
		if (source.getPoints() != null)
			target.setPoints(source.getPoints());
	}

}
