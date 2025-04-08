package pl.polsl.FootballLeague.controller;

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

import pl.polsl.FootballLeague.model.Position;
import pl.polsl.FootballLeague.repository.PositionRepository;

@Controller
@RequestMapping("/position")
public class PositionController {
	@Autowired
	PositionRepository positionRepo;

	@PostMapping
	public @ResponseBody void addPosition(@RequestBody Position position) {
		positionRepo.save(position);
	}

	@GetMapping
	public @ResponseBody Iterable<Position> getPositions() {
		return positionRepo.findAll();
	}

	@PutMapping("/{id}")
	public @ResponseBody ResponseEntity<?> updatePosition(@PathVariable Integer id,
			@RequestBody Position updatedPosition) {
		Optional<Position> optionalPosition = positionRepo.findById(id);

		if (optionalPosition.isPresent()) {
			Position existingPosition = optionalPosition.get();
			existingPosition.setName(updatedPosition.getName());
			positionRepo.save(existingPosition);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

}
