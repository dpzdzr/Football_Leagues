package pl.polsl.FootballLeague.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoalCreateDTO extends RepresentationModel<GoalCreateDTO> {
	private Integer id;
	private Integer minuteScored;
	private Integer matchId;
	private Integer scorerId;
	private Integer assistantId;
}
