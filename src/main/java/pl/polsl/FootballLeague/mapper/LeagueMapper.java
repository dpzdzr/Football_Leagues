package pl.polsl.FootballLeague.mapper;

import pl.polsl.FootballLeague.dto.input.LeagueCreateDTO;
import pl.polsl.FootballLeague.model.League;
import pl.polsl.FootballLeague.util.DtoMappingUtil;

public class LeagueMapper {
	public static void updateFromDto(LeagueCreateDTO dto, League league) {
		league.setName(dto.getName());
		league.setCountry(dto.getCountry());
	}
	
	public static void patchFromDto(LeagueCreateDTO dto, League league) {
		DtoMappingUtil.copyIfNotNull(dto.getName(), league::setName);
		DtoMappingUtil.copyIfNotNull(dto.getCountry(), league::setCountry);
	}
}
