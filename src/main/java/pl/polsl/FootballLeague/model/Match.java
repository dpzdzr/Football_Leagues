package pl.polsl.FootballLeague.model;

import java.time.LocalDateTime;
import java.util.List;

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
public class Match {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDateTime dateTime;

	@ManyToOne(optional = false)
	private Club homeClub;

	@ManyToOne(optional = false)
	private Club awayClub;

	@OneToMany(mappedBy = "match")
	private List<Goal> goals;
}
