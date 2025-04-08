package pl.polsl.FootballLeague.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class Kredyt2 {
	private Double kwota;
	private Double procent;
	private Integer liczbaRat;
	private Double rata;

	public double obliczRate(double kwota, double procent, int liczbaRat) {
		this.kwota = kwota;
		this.procent = procent;
		this.liczbaRat = liczbaRat;
		double m = 1 - 1 / Math.pow(1.0 + procent / 12, liczbaRat);
		this.rata = kwota * (procent / 12) / m;

		return rata;
	}

	public double obliczRate() {
		double m = 1 - 1 / Math.pow(1.0 + procent / 12, liczbaRat);
		this.rata = kwota * (procent / 12) / m;

		return rata;
	}

}
