package pl.polsl.FootballLeague.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	public @ResponseBody Iterable<Position> getPositions(){
		return positionRepo.findAll();
	}
}
