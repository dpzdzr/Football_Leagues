package pl.polsl.FootballLeague.mapper;

import pl.polsl.FootballLeague.dto.input.PlayerCreateDTO;
import pl.polsl.FootballLeague.model.Player;
import pl.polsl.FootballLeague.util.DtoMappingUtil;

public class PlayerMapper {
	public static void updateFromDto(PlayerCreateDTO dto, Player player) {
		player.setFirstName(dto.getFirstName());
		player.setLastName(dto.getLastName());
		player.setBirthdayDate(dto.getBirthdayDate());
	}
	
	public static void patchFromDto(PlayerCreateDTO dto, Player player) {
		DtoMappingUtil.copyIfNotNull(dto.getFirstName(), player::setFirstName);
		DtoMappingUtil.copyIfNotNull(dto.getLastName(), player::setLastName);
		DtoMappingUtil.copyIfNotNull(dto.getBirthdayDate(), player::setBirthdayDate);
	}
}
