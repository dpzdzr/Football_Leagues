package pl.polsl.FootballLeague.mapper;

import pl.polsl.FootballLeague.dto.input.GoalCreateDTO;
import pl.polsl.FootballLeague.model.Goal;
import pl.polsl.FootballLeague.util.DtoMappingUtil;

public class GoalMapper {
	public static void updateFromDTO(GoalCreateDTO dto, Goal goal) {
		goal.setMinuteScored(dto.getMinuteScored());
	}

	public static void patchFromDTO(GoalCreateDTO dto, Goal goal) {
		DtoMappingUtil.copyIfNotNull(dto.getMinuteScored(), goal::setMinuteScored);
	}
}
