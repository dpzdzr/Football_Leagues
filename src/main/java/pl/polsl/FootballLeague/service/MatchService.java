package pl.polsl.FootballLeague.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pl.polsl.FootballLeague.dto.input.MatchCreateDTO;
import pl.polsl.FootballLeague.dto.output.ClubDTO;
import pl.polsl.FootballLeague.dto.output.GoalDTO;
import pl.polsl.FootballLeague.dto.output.MatchDTO;
import pl.polsl.FootballLeague.mapper.MatchMapper;
import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.Match;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.MatchRepository;
import pl.polsl.FootballLeague.util.DtoMappingUtil;
import pl.polsl.FootballLeague.util.RepoUtil;

@Service
@RequiredArgsConstructor
public class MatchService {
	private final MatchRepository matchRepo;
	private final ClubRepository clubRepo;

	public List<MatchDTO> getAll() {
		return StreamSupport.stream(matchRepo.findAll().spliterator(), false).map(MatchDTO::new).toList();
	}

	public MatchDTO getById(Integer id) {
		return new MatchDTO(findMatch(id));
	}

	public ClubDTO getHomeClub(Integer id) {
		return Optional.ofNullable(findMatch(id).getHomeClub()).map(ClubDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Home club is not assigned"));
	}

	public ClubDTO getAwayClub(Integer id) {
		return Optional.ofNullable(findMatch(id).getAwayClub()).map(ClubDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Away club is not assigned"));
	}

	public List<GoalDTO> getGoals(Integer id) {
		return findMatch(id).getGoals().stream().map(GoalDTO::new).toList();
	}

	@Transactional
	public MatchDTO create(MatchCreateDTO dto) {
		Match match = new Match();
		MatchMapper.updateFromDTO(dto, match);
		assignRelations(dto, match, true);
		adjustPointsByResult(match, true);
		return new MatchDTO(matchRepo.save(match));
	}

	@Transactional
	public MatchDTO update(Integer id, MatchCreateDTO dto) {
		Match match = findMatch(id);
		MatchMapper.updateFromDTO(dto, match);
		assignRelations(dto, match, true);
		return new MatchDTO(matchRepo.save(match));
	}

	@Transactional
	public MatchDTO patch(Integer id, MatchCreateDTO dto) {
		Match match = findMatch(id);
		MatchMapper.patchFromDTO(dto, match);
		assignRelations(dto, match, false);
		return new MatchDTO(matchRepo.save(match));
	}

	@Transactional
	public void delete(Integer id) {
		matchRepo.delete(findMatch(id));
	}

	private Match findMatch(Integer id) {
		return RepoUtil.findOrThrow(matchRepo.findById(id), Match.class);
	}

	private void assignRelations(MatchCreateDTO dto, Match match, boolean evenIfNull) {
		if (evenIfNull) {
			DtoMappingUtil.assignEvenIfNull(clubRepo, dto.getHomeClubId(), match::setHomeClub, Club.class);
			DtoMappingUtil.assignEvenIfNull(clubRepo, dto.getAwayClubId(), match::setAwayClub, Club.class);
		} else {
			DtoMappingUtil.assignIfNotNull(clubRepo, dto.getHomeClubId(), match::setHomeClub, Club.class);
			DtoMappingUtil.assignIfNotNull(clubRepo, dto.getAwayClubId(), match::setAwayClub, Club.class);
		}
	}

	private void adjustPointsByResult(Match match, boolean add) {		
		if (match.getHomeScore() == null || match.getAwayScore() == null)
			return;

		if (match.getHomeScore() > match.getAwayScore()) {
			if(add)
				match.getHomeClub().addWin();
			else 
				match.getHomeClub().substractWin();
		} else if (match.getHomeScore() < match.getAwayScore()) {
			if(add)
				match.getAwayClub().addWin();
			else 
				match.getAwayClub().substractWin();
		} else {
	        if (add) {
	            match.getHomeClub().addDraw();
	            match.getAwayClub().addDraw();
	        } else {
	            match.getHomeClub().substractDraw();
	            match.getAwayClub().substractDraw();
	        }
		}
		clubRepo.save(match.getHomeClub());
		clubRepo.save(match.getAwayClub());
	}
}
