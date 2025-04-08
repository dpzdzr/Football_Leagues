package pl.polsl.FootballLeague.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Goal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer minuteScored;

	@ManyToOne
	private Match match;

	@ManyToOne
	private Player scorer;

	@ManyToOne
	private Player assistant;
}
