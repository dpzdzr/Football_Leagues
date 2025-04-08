package pl.polsl.FootballLeague.model;

import org.springframework.stereotype.Component;

@Component
public class Kredyt1 {
	public double obliczRate(double kwota, double procent, double raty) {
		double m = 1 - 1 / Math.pow(1.0 + procent / 12, raty);
		double rata = kwota * (procent / 12) / m;
		return rata;
	}
}
