package pl.polsl.FootballLeague.dto.input;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerCreateDTO {
	private String firstName;
	private String lastName;
	private LocalDate birthdayDate;
	private Integer clubId;
	private Integer positionId;
}
