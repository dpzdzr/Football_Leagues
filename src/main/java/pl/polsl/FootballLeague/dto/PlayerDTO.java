package pl.polsl.FootballLeague.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import pl.polsl.FootballLeague.controller.PlayerController;
import pl.polsl.FootballLeague.model.Player;

@Getter
public class PlayerDTO extends RepresentationModel<PlayerDTO> {
	private Integer id;
	private String firstName;
	private String lastName;
	private LocalDate birthDayDate;

	public PlayerDTO(Player player) {
		super();
		this.id = player.getId();
		this.firstName = player.getFirstName();
		this.lastName = player.getLastName();
		this.birthDayDate = player.getBirthdayDate();
		
		this.add(linkTo(methodOn(PlayerController.class).getClubForPlayer(id)).withRel("club"));
		this.add(linkTo(methodOn(PlayerController.class).getPositionForPlayer(id)).withRel("position"));
		this.add(linkTo(methodOn(PlayerController.class).getGoalsForPlayer(id)).withRel("goals"));
	}

}
