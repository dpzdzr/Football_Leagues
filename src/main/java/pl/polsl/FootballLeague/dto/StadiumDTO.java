package pl.polsl.FootballLeague.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import pl.polsl.FootballLeague.controller.StadiumController;
import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.Stadium;

@Getter
public class StadiumDTO extends RepresentationModel<StadiumDTO> {
	private Integer id;
	private String name;
	private Integer capacity;
	private String address;
	private Club club;

	public StadiumDTO(Stadium stadium) {
		this.id = stadium.getId();
		this.name = stadium.getName();
		this.capacity = stadium.getCapacity();
		this.address = stadium.getAddress();
		this.club = stadium.getClub();
		
		this.add(linkTo(methodOn(StadiumController.class).getStadium(id)).withSelfRel());
		this.add(linkTo(methodOn(StadiumController.class).getClubForStadium(id)).withRel("club"));
		this.add(linkTo(methodOn(StadiumController.class).updateStadium(id, null)).withRel("update").withType("PUT"));
		this.add(linkTo(methodOn(StadiumController.class).patchStadium(id, null)).withRel("patch").withType("PATCH"));
		this.add(linkTo(methodOn(StadiumController.class).deleteStadium(id)).withRel("delete").withType("DELETE"));
	}

}
