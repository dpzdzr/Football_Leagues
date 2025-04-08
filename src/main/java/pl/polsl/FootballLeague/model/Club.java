package pl.polsl.FootballLeague.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Club {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String city;
	private Integer foundationYear;
	private Integer currentPosition;

	@ManyToOne
	private League league;

	@OneToOne
	private Stadium stadium;

	@OneToMany(mappedBy = "club")
	private List<Player> players;

	@OneToMany(mappedBy = "homeClub")
	private List<Match> homeMatches;

	@OneToMany(mappedBy = "awayClub")
	private List<Match> awayMatches;
}
