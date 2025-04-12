package pl.polsl.FootballLeague.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import pl.polsl.FootballLeague.controller.StadiumController;
import pl.polsl.FootballLeague.model.Stadium;

@Getter
public class StadiumDTO extends RepresentationModel<StadiumDTO> {
	private Integer id;
	private String name;
	private Integer capacity;
	private String address;

	public StadiumDTO(Stadium stadium) {
		this.id = stadium.getId();
		this.name = stadium.getName();
		this.capacity = stadium.getCapacity();
		this.address = stadium.getAddress();

		this.add(linkTo(methodOn(StadiumController.class).getStadium(id)).withSelfRel());
		this.add(linkTo(methodOn(StadiumController.class).getClubForStadium(id)).withRel("club"));
	}

}
