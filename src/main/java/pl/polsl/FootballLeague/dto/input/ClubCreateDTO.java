package pl.polsl.FootballLeague.dto.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubCreateDTO {
	private Integer id;
	private String name;
	private String city;
	private Integer foundationYear;
	private Integer points;
	private Integer stadiumId;
	private Integer leagueId;
}
