package pl.polsl.FootballLeague.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String firstName;
	private String lastName;
	private LocalDate birthdayDate;

	@ManyToOne
	private Club club;

	@ManyToOne
	private Position position;

	@OneToMany(mappedBy = "scorer")
	@JsonIgnore
	private List<Goal> goals;

	@OneToMany(mappedBy = "assistant")
	@JsonIgnore
	private List<Goal> assists;
}
