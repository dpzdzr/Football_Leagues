package pl.polsl.FootballLeague.mapper;

import pl.polsl.FootballLeague.dto.input.MatchCreateDTO;
import pl.polsl.FootballLeague.model.Match;
import pl.polsl.FootballLeague.util.DtoMappingUtil;

public class MatchMapper {
	public static void updateFromDTO(MatchCreateDTO dto, Match match) {
		match.setDateTime(dto.getDateTime());
		match.setHomeScore(dto.getHomeScore());
		match.setAwayScore(dto.getAwayScore());
	}

	public static void patchFromDTO(MatchCreateDTO dto, Match match) {
		DtoMappingUtil.copyIfNotNull(dto.getDateTime(), match::setDateTime);
		DtoMappingUtil.copyIfNotNull(dto.getHomeScore(), match::setHomeScore);
	}
}
