package pl.polsl.FootballLeague.mapper;

import static pl.polsl.FootballLeague.util.DtoMappingUtil.assignEvenIfNull;
import static pl.polsl.FootballLeague.util.DtoMappingUtil.assignIfNotNull;
import static pl.polsl.FootballLeague.util.DtoMappingUtil.copyIfNotNull;

import pl.polsl.FootballLeague.dto.ClubCreateDTO;
import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.LeagueRepository;
import pl.polsl.FootballLeague.repository.StadiumRepository;

public class ClubMapper {
	public static void toEntity(ClubCreateDTO dto, Club club, StadiumRepository stadiumRepo,
			LeagueRepository leagueRepo) {
		club.setName(dto.getName());
		club.setCity(dto.getCity());
		club.setFoundationYear(dto.getFoundationYear());
		club.setPoints(dto.getPoints());

		assignIfNotNull(stadiumRepo, dto.getStadiumId(), club::setStadium, "Stadium");
		assignIfNotNull(leagueRepo, dto.getLeagueId(), club::setLeague, "League");
	}

	public static void put(ClubCreateDTO dto, Club club, ClubRepository clubRepo, StadiumRepository stadiumRepo,
			LeagueRepository leagueRepo) {
		club.setName(dto.getName());
		club.setCity(dto.getCity());
		club.setFoundationYear(dto.getFoundationYear());
		club.setPoints(dto.getPoints());

		assignEvenIfNull(stadiumRepo, dto.getStadiumId(), club::setStadium, "Stadium");
		assignEvenIfNull(leagueRepo, dto.getLeagueId(), club::setLeague, "League");
	}

	public static void patch(ClubCreateDTO dto, Club club, StadiumRepository stadiumRepo, LeagueRepository leagueRepo) {
		copyIfNotNull(dto.getName(), club::setName);
		copyIfNotNull(dto.getCity(), club::setCity);
		copyIfNotNull(dto.getFoundationYear(), club::setFoundationYear);
		copyIfNotNull(dto.getPoints(), club::setPoints);

		assignIfNotNull(stadiumRepo, dto.getStadiumId(), club::setStadium, "Stadium");
		assignIfNotNull(leagueRepo, dto.getLeagueId(), club::setLeague, "League");
	}
}
