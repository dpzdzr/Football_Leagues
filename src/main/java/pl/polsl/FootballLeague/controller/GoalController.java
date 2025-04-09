package pl.polsl.FootballLeague.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.polsl.FootballLeague.model.Goal;
import pl.polsl.FootballLeague.repository.GoalRepository;

@Controller
@RequestMapping("/goal")
public class GoalController {
	@Autowired
	GoalRepository goalRepo;

	@GetMapping
	public @ResponseBody Iterable<Goal> getGoals() {
		return goalRepo.findAll();
	}
	
	@PostMapping
	public @ResponseBody void addGoal(@RequestBody Goal goal) {
		goalRepo.save(goal);
	}
}
