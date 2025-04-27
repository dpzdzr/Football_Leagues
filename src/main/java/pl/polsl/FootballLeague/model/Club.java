package pl.polsl.FootballLeague.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private Integer points;

	@ManyToOne
	private League league;

	@ManyToOne
	private Stadium stadium;

	@OneToMany(mappedBy = "club")
	@JsonIgnore
	private List<Player> players;

	@OneToMany(mappedBy = "homeClub")
	@JsonIgnore
	private List<Match> homeMatches;

	@OneToMany(mappedBy = "awayClub")
	@JsonIgnore
	private List<Match> awayMatches;

	public void addWin() {
		points += 3;
	}

	public void addDraw() {
		points += 1;
	}
	
	public void substractWin() {
		points -= 3;
	}
	
	public void substractDraw() {
		points -= 1;
	}
}
