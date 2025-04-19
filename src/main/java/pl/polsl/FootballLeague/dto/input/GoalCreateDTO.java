package pl.polsl.FootballLeague.dto.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoalCreateDTO {
	private Integer id;
	private Integer minuteScored;
	private Integer matchId;
	private Integer scorerId;
	private Integer assistantId;
}
