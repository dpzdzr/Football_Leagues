package pl.polsl.FootballLeague.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pl.polsl.FootballLeague.dto.input.LeagueCreateDTO;
import pl.polsl.FootballLeague.dto.output.ClubDTO;
import pl.polsl.FootballLeague.dto.output.LeagueDTO;
import pl.polsl.FootballLeague.mapper.LeagueMapper;
import pl.polsl.FootballLeague.model.League;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.LeagueRepository;
import pl.polsl.FootballLeague.util.DeleteUtil;
import pl.polsl.FootballLeague.util.RepoUtil;

@Service
@RequiredArgsConstructor
public class LeagueService {
	private final LeagueRepository leagueRepo;
	private final ClubRepository clubRepo;

	public List<LeagueDTO> getAll() {
		return StreamSupport.stream(leagueRepo.findAll().spliterator(), false).map(LeagueDTO::new).toList();
	}

	public LeagueDTO getById(Integer id) {
		return new LeagueDTO(findLeague(id));
	}

	public List<ClubDTO> getTable(Integer id) {
		League league = findLeague(id);
		List<ClubDTO> table = league.getClubs().stream()
				.sorted(Comparator.comparingInt(c -> -Optional.ofNullable(c.getPoints()).orElse(0))).map(ClubDTO::new)
				.toList();
		return table;
	}

	public List<ClubDTO> getClubs(Integer id) {
		League league = findLeague(id);
		List<ClubDTO> clubDTOs = league.getClubs().stream().map(ClubDTO::new).toList();
		return clubDTOs;
	}

	@Transactional
	public LeagueDTO create(LeagueCreateDTO dto) {
		League league = new League();
		LeagueMapper.updateFromDto(dto, league);
		return new LeagueDTO(leagueRepo.save(league));
	}

	@Transactional
	public LeagueDTO update(Integer id, LeagueCreateDTO dto) {
		League league = findLeague(id);
		LeagueMapper.updateFromDto(dto, league);
		return new LeagueDTO(leagueRepo.save(league));
	}
	
	@Transactional
	public LeagueDTO patch(Integer id, LeagueCreateDTO dto) {
		League league = findLeague(id);
		LeagueMapper.patchFromDto(dto, league);
		return new LeagueDTO(leagueRepo.save(league));
	}

	@Transactional
	public void delete(Integer id) {
		League league = findLeague(id);
		DeleteUtil.detachCollection(league, League::getClubs, club -> club.setLeague(null), clubRepo);
		leagueRepo.delete(league);
	}
	
	private League findLeague(Integer id) {
		return RepoUtil.findOrThrow(leagueRepo.findById(id), League.class);
	}
}
