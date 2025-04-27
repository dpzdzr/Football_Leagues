package pl.polsl.FootballLeague.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pl.polsl.FootballLeague.dto.input.ClubCreateDTO;
import pl.polsl.FootballLeague.dto.output.ClubDTO;
import pl.polsl.FootballLeague.dto.output.LeagueDTO;
import pl.polsl.FootballLeague.dto.output.MatchDTO;
import pl.polsl.FootballLeague.dto.output.PlayerDTO;
import pl.polsl.FootballLeague.dto.output.StadiumDTO;
import pl.polsl.FootballLeague.mapper.ClubMapper;
import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.League;
import pl.polsl.FootballLeague.model.Player;
import pl.polsl.FootballLeague.model.Stadium;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.LeagueRepository;
import pl.polsl.FootballLeague.repository.MatchRepository;
import pl.polsl.FootballLeague.repository.PlayerRepository;
import pl.polsl.FootballLeague.repository.StadiumRepository;
import pl.polsl.FootballLeague.util.DeleteUtil;
import pl.polsl.FootballLeague.util.DtoMappingUtil;
import pl.polsl.FootballLeague.util.RepoUtil;

@Service
@RequiredArgsConstructor
public class ClubService {
	private final ClubRepository clubRepo;
	private final LeagueRepository leagueRepo;
	private final StadiumRepository stadiumRepo;
	private final PlayerRepository playerRepo;
	private final MatchRepository matchRepo;

	public List<ClubDTO> getAll() {
		List<ClubDTO> clubDTOs = new ArrayList<>();
		for (Club club : clubRepo.findAll())
			clubDTOs.add(new ClubDTO(club));
		return clubDTOs;
	}

	public ClubDTO getById(Integer id) {
		return new ClubDTO(RepoUtil.findOrThrow(clubRepo.findById(id), Club.class));
	}

	public List<PlayerDTO> getPlayers(Integer id) {
		Club club = RepoUtil.findOrThrow(clubRepo.findById(id), Club.class);
		List<PlayerDTO> playerDTOs = new ArrayList<>();
		for (Player player : club.getPlayers())
			playerDTOs.add(new PlayerDTO(player));
		return playerDTOs;
	}

	public LeagueDTO getLeague(Integer id) {
		Club club = RepoUtil.findOrThrow(clubRepo.findById(id), Club.class);
		League league = Optional.of(club.getLeague())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "League not assigned"));
		return new LeagueDTO(league);
	}

	public List<MatchDTO> getHomeMatches(Integer id) {
		Club club = RepoUtil.findOrThrow(clubRepo.findById(id), Club.class);
		List<MatchDTO> matchDTOs = StreamSupport.stream(club.getHomeMatches().spliterator(), false).map(MatchDTO::new)
				.toList();
		return matchDTOs;
	}

	public List<MatchDTO> getAwayMatches(Integer id) {
		Club club = RepoUtil.findOrThrow(clubRepo.findById(id), Club.class);
		List<MatchDTO> matchDTOs = StreamSupport.stream(club.getAwayMatches().spliterator(), false).map(MatchDTO::new)
				.toList();
		return matchDTOs;
	}

	public StadiumDTO getStadium(Integer id) {
		Club club = RepoUtil.findOrThrow(clubRepo.findById(id), Club.class);
		return Optional.ofNullable(club.getStadium()).map(StadiumDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stadium not assigned"));
	}

	@Transactional
	public ClubDTO create(ClubCreateDTO dto) {
		Club club = new Club();
		ClubMapper.updateFromDTO(dto, club);
		assignRelations(club, dto, true);
		return new ClubDTO(clubRepo.save(club));
	}

	@Transactional
	public ClubDTO update(Integer id, ClubCreateDTO dto) {
		Club club = RepoUtil.findOrThrow(clubRepo.findById(id), Club.class);
		ClubMapper.updateFromDTO(dto, club);
		assignRelations(club, dto, true);
		return new ClubDTO(clubRepo.save(club));
	}

	@Transactional
	public ClubDTO patch(Integer id, ClubCreateDTO dto) {
		Club club = RepoUtil.findOrThrow(clubRepo.findById(id), Club.class);
		ClubMapper.patchFromDTO(dto, club);
		assignRelations(club, dto, false);
		return new ClubDTO(clubRepo.save(club));
	}
	
	@Transactional
	public void delete(Integer id) {
		Club club = RepoUtil.findOrThrow(clubRepo.findById(id), Club.class);
		DeleteUtil.detachCollection(club, Club::getPlayers, (p -> p.setClub(null)), playerRepo);
		DeleteUtil.detachCollection(club, Club::getHomeMatches, (m -> m.setHomeClub(null)), matchRepo);
		DeleteUtil.detachCollection(club, Club::getAwayMatches, (m -> m.setAwayClub(null)), matchRepo);
		DeleteUtil.detachSingle(club, Club::getStadium, (stadium, c) -> stadium.getClubs().remove(c), stadiumRepo);
		DeleteUtil.detachSingle(club, Club::getLeague, (league, c) -> league.getClubs().remove(c), leagueRepo);
		club.setLeague(null);
		club.setStadium(null);
		clubRepo.delete(club);
	}

	private void assignRelations(Club club, ClubCreateDTO dto, boolean evenIfNull) {
		if (evenIfNull) {
			DtoMappingUtil.assignEvenIfNull(leagueRepo, dto.getLeagueId(), club::setLeague, League.class);
			DtoMappingUtil.assignEvenIfNull(stadiumRepo, dto.getStadiumId(), club::setStadium, Stadium.class);
		} else {
			DtoMappingUtil.assignIfNotNull(leagueRepo, dto.getLeagueId(), club::setLeague, League.class);
			DtoMappingUtil.assignIfNotNull(stadiumRepo, dto.getStadiumId(), club::setStadium, Stadium.class);
		}
	}

}
