package pl.polsl.FootballLeague.dto.output;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import pl.polsl.FootballLeague.controller.LeagueController;
import pl.polsl.FootballLeague.model.League;

@Getter
public class LeagueDTO extends RepresentationModel<LeagueDTO> {
	private Integer id;
	private String name;
	private String country;

	public LeagueDTO(League league) {
		super();
		this.id = league.getId();
		this.name = league.getName();
		this.country = league.getCountry();
		
		this.add(linkTo(methodOn(LeagueController.class).getLeague(id)).withSelfRel());
		this.add(linkTo(methodOn(LeagueController.class).getClubsForLeague(id)).withRel("clubs"));
		this.add(linkTo(methodOn(LeagueController.class).getLeagueTable(id)).withRel("table"));
	}
}
