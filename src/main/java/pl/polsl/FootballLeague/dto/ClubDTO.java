package pl.polsl.FootballLeague.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import pl.polsl.FootballLeague.controller.ClubController;
import pl.polsl.FootballLeague.model.Club;

@Getter
public class ClubDTO extends RepresentationModel<ClubDTO> {
	
	private Integer id;
	private String name;
	private String city;
	private Integer foundationYear;
	private Integer points;
	
	public ClubDTO(Club club) {
		super();
		this.id = club.getId();
		this.name = club.getName();
		this.city = club.getCity();
		this.foundationYear = club.getFoundationYear();
		this.points = club.getPoints();
		
		this.add(linkTo(methodOn(ClubController.class).getClub(id)).withSelfRel());
		this.add(linkTo(methodOn(ClubController.class).getLeagueForClub(id)).withRel("league"));
		this.add(linkTo(methodOn(ClubController.class).getPlayersForClub(id)).withRel("players"));
	}
}
