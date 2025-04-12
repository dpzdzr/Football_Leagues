package pl.polsl.FootballLeague.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pl.polsl.FootballLeague.dto.ClubDTO;
import pl.polsl.FootballLeague.dto.LeagueDTO;
import pl.polsl.FootballLeague.dto.PlayerDTO;
import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.League;
import pl.polsl.FootballLeague.model.Player;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.LeagueRepository;

@RestController
@RequestMapping("/club")
public class ClubController {
	@Autowired
	private ClubRepository clubRepo;
	@Autowired
	private LeagueRepository leagueRepo;

	@GetMapping
	public CollectionModel<ClubDTO> getClubs() {
		List<ClubDTO> clubsDTO = new ArrayList<>();
		for (Club club : clubRepo.findAll())
			clubsDTO.add(new ClubDTO(club));

		return CollectionModel.of(clubsDTO);
	}

	@GetMapping("/{id}")
	public ClubDTO getClub(@PathVariable Integer id) {
		return new ClubDTO(findClubOrThrowException(id));
	}

	@GetMapping("/{id}/players")
	public CollectionModel<PlayerDTO> getPlayersForClub(@PathVariable Integer id) {
		Club club = findClubOrThrowException(id);
		List<PlayerDTO> playersDTO = new ArrayList<>();

		for (Player player : club.getPlayers())
			playersDTO.add(new PlayerDTO(player));

		return CollectionModel.of(playersDTO);
	}

	@GetMapping("/{id}/league")
	public LeagueDTO getLeagueForClub(@PathVariable Integer id) {
		Club club = findClubOrThrowException(id);
		League league = club.getLeague();
		if (league == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "League not assigned");
		return (new LeagueDTO(club.getLeague()));
	}

	@PutMapping("/{id}")
	public ClubDTO updateClub(@PathVariable Integer id, @RequestBody Club updatedClub) {
		Club existingClub = findClubOrThrowException(id);
		updateNotNullFields(updatedClub, existingClub);
		clubRepo.save(existingClub);

		return (new ClubDTO(existingClub));
	}

	@PostMapping
	public void addClub(@RequestBody Club club) {
		clubRepo.save(club);
	}

	@PostMapping("/batch")
	public void addClubs(@RequestBody List<Club> clubs) {
		clubRepo.saveAll(clubs);
	}

	private Club findClubOrThrowException(Integer id) {
		return clubRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not found"));
	}

	private void updateNotNullFields(Club source, Club target) {
		copyIfNotNull(source.getName(), target::setName);
		copyIfNotNull(source.getCity(), target::setCity);
		copyIfNotNull(source.getFoundationYear(), target::setFoundationYear);
		copyIfNotNull(source.getPoints(), target::setPoints);
		if (source.getLeague() != null && source.getLeague().getId() != null) {
			League league = leagueRepo.findById(source.getLeague().getId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "League not found"));
			target.setLeague(league);
		}
	}

	private <T> void copyIfNotNull(T value, Consumer<T> setter) {
		if (value != null) {
			setter.accept(value);
		}
	}

}
