package pl.polsl.FootballLeague.dto.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StadiumCreateDTO {
	private String name;
	private Integer capacity;
	private String address;
}
