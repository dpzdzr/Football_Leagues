package pl.polsl.FootballLeague.mapper;

import pl.polsl.FootballLeague.dto.input.StadiumCreateDTO;
import pl.polsl.FootballLeague.model.Stadium;
import pl.polsl.FootballLeague.util.DtoMappingUtil;

public class StadiumMapper {
	public static void updateFromDto(StadiumCreateDTO dto, Stadium stadium) {
		stadium.setName(dto.getName());
		stadium.setCapacity(dto.getCapacity());
		stadium.setAddress(dto.getAddress());
	}

	public static void patchFromDto(StadiumCreateDTO dto, Stadium stadium) {
		DtoMappingUtil.copyIfNotNull(dto.getName(), stadium::setName);
		DtoMappingUtil.copyIfNotNull(dto.getCapacity(), stadium::setCapacity);
		DtoMappingUtil.copyIfNotNull(dto.getAddress(), stadium::setAddress);
	}
}
