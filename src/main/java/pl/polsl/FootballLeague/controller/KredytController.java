package pl.polsl.FootballLeague.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.polsl.FootballLeague.model.Kredyt1;
import pl.polsl.FootballLeague.model.Kredyt2;

@Controller
public class KredytController {
	@Autowired
	Kredyt1 kredyt1;
	@Autowired
	Kredyt2 kredyt2;

	@GetMapping("kredyt1")
	public @ResponseBody Double getKredyt1(@RequestParam Double kwota) {
		return kredyt1.obliczRate(kwota, 0.1, 12);
	}

	@GetMapping("/kredyt2")
	public @ResponseBody Kredyt2 getKredyt2(@RequestParam Double kwota) {
		kredyt2.obliczRate(kwota, 0.1, 12);
		return kredyt2;
	}

	@PostMapping("/kredyt3")
	public @ResponseBody Kredyt2 getKredyt3(@RequestBody Kredyt2 kredyt) {
		kredyt.obliczRate();
		System.out.println(kredyt.getKwota() + ", " + kredyt.getProcent() + ", " + kredyt.getLiczbaRat() + ", "
				+ kredyt.getRata());
		return kredyt;
	}

}
