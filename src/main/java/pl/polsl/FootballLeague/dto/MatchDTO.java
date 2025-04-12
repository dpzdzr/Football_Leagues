package pl.polsl.FootballLeague.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import pl.polsl.FootballLeague.controller.MatchController;
import pl.polsl.FootballLeague.model.Match;

@Getter
public class MatchDTO extends RepresentationModel<MatchDTO> {
	private Integer id;
	private LocalDateTime dateTime;
	private Integer homeScore;
	private Integer awayScore;

	public MatchDTO(Match match) {
		this.id = match.getId();
		this.dateTime = match.getDateTime();
		this.homeScore = match.getHomeScore();
		this.awayScore = match.getAwayScore();
		
		this.add(linkTo(methodOn(MatchController.class).getMatch(id)).withSelfRel());
		this.add(linkTo(methodOn(MatchController.class).getHomeClubForMatch(id)).withRel("home_club"));
		this.add(linkTo(methodOn(MatchController.class).getAwayClubForMatch(id)).withRel("away_club"));
		this.add(linkTo(methodOn(MatchController.class).getGoalsForMatch(id)).withRel("goals"));
	}

}
