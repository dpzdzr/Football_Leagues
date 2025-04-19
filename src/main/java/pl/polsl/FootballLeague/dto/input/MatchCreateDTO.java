package pl.polsl.FootballLeague.dto.input;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MatchCreateDTO {
	private LocalDateTime dateTime;
	private Integer homeScore;
	private Integer awayScore;
	private Integer homeClubId;
	private Integer awayClubId;
}
