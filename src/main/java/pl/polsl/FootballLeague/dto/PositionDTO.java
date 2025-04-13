package pl.polsl.FootballLeague.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import pl.polsl.FootballLeague.controller.PositionController;
import pl.polsl.FootballLeague.model.Position;

@Getter
public class PositionDTO extends RepresentationModel<PositionDTO> {
	private Integer id;
	private String name;

	public PositionDTO(Position position) {
		super();
		this.id = position.getId();
		this.name = position.getName();
		
		this.add(linkTo(methodOn(PositionController.class).getPosition(id)).withSelfRel());
		this.add(linkTo(methodOn(PositionController.class).getPlayersForPosition(id)).withRel("players"));
		this.add(linkTo(methodOn(PositionController.class).updatePosition(id, null)).withRel("update").withType("PUT"));
		this.add(linkTo(methodOn(PositionController.class).patchPosition(id, null)).withRel("patch").withType("PATCH"));
		this.add(linkTo(methodOn(PositionController.class).deletePositionById(id)).withRel("delete").withType("DELETE"));
	}
}
