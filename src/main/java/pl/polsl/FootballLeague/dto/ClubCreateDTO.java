package pl.polsl.FootballLeague.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubCreateDTO extends RepresentationModel<ClubCreateDTO> {
	private Integer id;
	private String name;
	private String city;
	private Integer foundationYear;
	private Integer points;
	private Integer stadiumId;
	private Integer leagueId;
}
