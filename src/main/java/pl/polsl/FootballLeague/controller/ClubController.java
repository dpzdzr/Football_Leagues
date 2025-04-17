package pl.polsl.FootballLeague.controller;

import static pl.polsl.FootballLeague.util.RepositoryUtil.findOrThrow;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pl.polsl.FootballLeague.dto.ClubCreateDTO;
import pl.polsl.FootballLeague.dto.ClubDTO;
import pl.polsl.FootballLeague.dto.LeagueDTO;
import pl.polsl.FootballLeague.dto.MatchDTO;
import pl.polsl.FootballLeague.dto.PlayerDTO;
import pl.polsl.FootballLeague.dto.StadiumDTO;
import pl.polsl.FootballLeague.mapper.ClubMapper;
import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.League;
import pl.polsl.FootballLeague.model.Player;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.LeagueRepository;
import pl.polsl.FootballLeague.repository.MatchRepository;
import pl.polsl.FootballLeague.repository.PlayerRepository;
import pl.polsl.FootballLeague.repository.StadiumRepository;
import pl.polsl.FootballLeague.util.DeleteUtil;

	@RestController
	@RequestMapping("/club")
	public class ClubController {
		@Autowired
		private ClubRepository clubRepo;
		@Autowired
		private LeagueRepository leagueRepo;
		@Autowired
		StadiumRepository stadiumRepo;
		@Autowired
		private PlayerRepository playerRepo;
		@Autowired
		private MatchRepository matchRepo;
	
		@PostMapping
		public ResponseEntity<ClubDTO> addClub(@RequestBody ClubCreateDTO clubCreateDTO) {
			Club club = new Club();
			ClubMapper.toEntity(clubCreateDTO, club, stadiumRepo, leagueRepo);
			ClubDTO clubDTO = new ClubDTO(clubRepo.save(club));
			return ResponseEntity.created(URI.create("/club/" + clubDTO.getId())).body(clubDTO);
		}
	
		@GetMapping
		public CollectionModel<ClubDTO> getClubs() {
			List<ClubDTO> clubsDTO = new ArrayList<>();
			for (Club club : clubRepo.findAll())
				clubsDTO.add(new ClubDTO(club));
			return CollectionModel.of(clubsDTO);
		}
	
		@GetMapping("/{id}")
		public ClubDTO getClub(@PathVariable Integer id) {
			return new ClubDTO(findOrThrow(clubRepo.findById(id), "Club"));
		}	
	
		@GetMapping("/{id}/players")
		public CollectionModel<PlayerDTO> getPlayersForClub(@PathVariable Integer id) {
			Club club = findOrThrow(clubRepo.findById(id), "Club");
			List<PlayerDTO> playerDTOs = new ArrayList<>();
			for (Player player : club.getPlayers())
				playerDTOs.add(new PlayerDTO(player));
			return CollectionModel.of(playerDTOs);
		}
	
		@GetMapping("/{id}/league")
		public LeagueDTO getLeagueForClub(@PathVariable Integer id) {
			Club club = findOrThrow(clubRepo.findById(id), "Club");
			League league = Optional.of(club.getLeague())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "League not assigned"));
			return new LeagueDTO(league);
		}
	
		@GetMapping("/{id}/home_matches")
		public CollectionModel<MatchDTO> getHomeMatchesForClub(@PathVariable Integer id) {
			Club club = findOrThrow(clubRepo.findById(id), "Club");
			List<MatchDTO> matchDTOs = StreamSupport.stream(club.getHomeMatches().spliterator(), false).map(MatchDTO::new)
					.toList();
			return CollectionModel.of(matchDTOs);
		}
	
		@GetMapping("/{id}/away_matches")
		public CollectionModel<MatchDTO> getAwayMatchesForClub(@PathVariable Integer id) {
			Club club = findOrThrow(clubRepo.findById(id), "Club");
			List<MatchDTO> matchDTOs = StreamSupport.stream(club.getAwayMatches().spliterator(), false).map(MatchDTO::new)
					.toList();
			return CollectionModel.of(matchDTOs);
		}
	
		@GetMapping("/{id}/stadium")
		public StadiumDTO getStadiumForClub(@PathVariable Integer id) {
			Club club = findOrThrow(clubRepo.findById(id), "Club");
			return Optional.ofNullable(club.getStadium()).map(StadiumDTO::new)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stadium not assigned"));
		}
	
		@PutMapping("/{id}")
		public ResponseEntity<ClubDTO> putClub(@PathVariable Integer id, @RequestBody ClubCreateDTO putClub) {
			Club existingClub = findOrThrow(clubRepo.findById(id), "Club");
			ClubMapper.put(putClub, existingClub, clubRepo, stadiumRepo, leagueRepo);
			return ResponseEntity.ok(new ClubDTO(clubRepo.save(existingClub)));
		}
	
		@PatchMapping("/{id}")
		public ResponseEntity<ClubDTO> patchClub(@PathVariable Integer id, @RequestBody ClubCreateDTO patchClub) {
			Club existingClub = findOrThrow(clubRepo.findById(id), "Club");
			ClubMapper.patch(patchClub, existingClub, stadiumRepo, leagueRepo);
			clubRepo.save(existingClub);
			return ResponseEntity.ok(new ClubDTO(existingClub));
		}
	
		@DeleteMapping("/{id}")
		public ResponseEntity<Object> deleteClub(@PathVariable Integer id) {
			Club club = findOrThrow(clubRepo.findById(id), "Club");
			DeleteUtil.detachCollection(club, Club::getPlayers, (p -> p.setClub(null)), playerRepo);
			DeleteUtil.detachCollection(club, Club::getHomeMatches, (m -> m.setHomeClub(null)), matchRepo);
			DeleteUtil.detachCollection(club, Club::getAwayMatches, (m -> m.setAwayClub(null)), matchRepo);
			DeleteUtil.detachSingle(club, Club::getStadium, stadium -> stadium.setClub(null), stadiumRepo);
			club.setStadium(null);
			DeleteUtil.detachSingle(club, Club::getLeague, (league, c)-> league.getClubs().remove(c), leagueRepo);
			club.setLeague(null);

			clubRepo.delete(club);
			return ResponseEntity.noContent().build();
		}
	}
