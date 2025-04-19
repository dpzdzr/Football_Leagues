package pl.polsl.FootballLeague.mapper;

import pl.polsl.FootballLeague.dto.input.PositionCreateDTO;
import pl.polsl.FootballLeague.model.Position;
import pl.polsl.FootballLeague.util.DtoMappingUtil;

public class PositionMapper {
	public static void updateFromDTO(PositionCreateDTO dto, Position position) {
		position.setName(dto.getName());
	}

	public static void patchFromDTO(PositionCreateDTO dto, Position position) {
		DtoMappingUtil.copyIfNotNull(dto.getName(), position::setName);
	}
}
