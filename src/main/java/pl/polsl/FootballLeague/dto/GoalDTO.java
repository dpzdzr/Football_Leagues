package pl.polsl.FootballLeague.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import pl.polsl.FootballLeague.controller.GoalController;
import pl.polsl.FootballLeague.model.Goal;

@Getter
public class GoalDTO extends RepresentationModel<GoalDTO> {
	private Integer id;
	private Integer minuteScored;

	public GoalDTO(Goal goal) {
		super();
		this.id = goal.getId();
		this.minuteScored = goal.getMinuteScored();
		
		this.add(linkTo(methodOn(GoalController.class).getGoal(id)).withSelfRel());
		this.add(linkTo(methodOn(GoalController.class).getMatchForGoal(id)).withRel("match"));
		this.add(linkTo(methodOn(GoalController.class).getScorerForGoal(id)).withRel("scorer"));
		this.add(linkTo(methodOn(GoalController.class).getAssistantForGoal(id)).withRel("assistant"));
	}
}
