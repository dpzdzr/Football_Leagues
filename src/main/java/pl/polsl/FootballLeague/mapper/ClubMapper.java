package pl.polsl.FootballLeague.mapper;

import pl.polsl.FootballLeague.dto.input.ClubCreateDTO;
import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.util.DtoMappingUtil;

public class ClubMapper {
	public static void updateFromDTO(ClubCreateDTO dto, Club club) {
		club.setName(dto.getName());
		club.setCity(dto.getCity());
		club.setFoundationYear(dto.getFoundationYear());
		club.setPoints(dto.getPoints());
	}

	public static void patchFromDTO(ClubCreateDTO dto, Club club) {
		DtoMappingUtil.copyIfNotNull(dto.getName(), club::setName);
		DtoMappingUtil.copyIfNotNull(dto.getCity(), club::setCity);
		DtoMappingUtil.copyIfNotNull(dto.getFoundationYear(), club::setFoundationYear);
		DtoMappingUtil.copyIfNotNull(dto.getPoints(), club::setPoints);
	}
}
