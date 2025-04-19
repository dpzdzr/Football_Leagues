package pl.polsl.FootballLeague.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pl.polsl.FootballLeague.dto.input.GoalCreateDTO;
import pl.polsl.FootballLeague.dto.output.GoalDTO;
import pl.polsl.FootballLeague.dto.output.MatchDTO;
import pl.polsl.FootballLeague.dto.output.PlayerDTO;
import pl.polsl.FootballLeague.mapper.GoalMapper;
import pl.polsl.FootballLeague.model.Goal;
import pl.polsl.FootballLeague.model.Match;
import pl.polsl.FootballLeague.model.Player;
import pl.polsl.FootballLeague.repository.GoalRepository;
import pl.polsl.FootballLeague.repository.MatchRepository;
import pl.polsl.FootballLeague.repository.PlayerRepository;
import pl.polsl.FootballLeague.util.DeleteUtil;
import pl.polsl.FootballLeague.util.DtoMappingUtil;
import pl.polsl.FootballLeague.util.RepoUtil;

@Service
@RequiredArgsConstructor
public class GoalService {
	private final GoalRepository goalRepo;
	private final MatchRepository matchRepo;
	private final PlayerRepository playerRepo;

	public List<GoalDTO> getAll() {
		List<GoalDTO> goalDTOs = new ArrayList<>();
		for (Goal goal : goalRepo.findAll())
			goalDTOs.add(new GoalDTO(goal));
		return goalDTOs;
	}

	public GoalDTO getById(Integer id) {
		return new GoalDTO(RepoUtil.findOrThrow(goalRepo.findById(id), Goal.class));
	}

	public MatchDTO getMatch(Integer id) {
		Goal goal = RepoUtil.findOrThrow(goalRepo.findById(id), Goal.class);
		return Optional.ofNullable(goal.getMatch()).map(MatchDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not assigned"));
	}

	public PlayerDTO getScorer(Integer id) {
		Goal goal = RepoUtil.findOrThrow(goalRepo.findById(id), Goal.class);
		return Optional.ofNullable(goal.getScorer()).map(PlayerDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scorer not assigned"));
	}

	public PlayerDTO getAssistant(Integer id) {
		Goal goal = RepoUtil.findOrThrow(goalRepo.findById(id), Goal.class);
		return Optional.ofNullable(goal.getAssistant()).map(PlayerDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assistant not assigned"));
	}

	@Transactional
	public GoalDTO create(GoalCreateDTO dto) {
		Goal goal = new Goal();
		GoalMapper.updateFromDTO(dto, goal);
		assignRelations(goal, dto, true);
		return new GoalDTO(goalRepo.save(goal));
	}

	@Transactional
	public GoalDTO update(Integer id, GoalCreateDTO dto) {
		Goal goal = RepoUtil.findOrThrow(goalRepo.findById(id), Goal.class);
		GoalMapper.updateFromDTO(dto, goal);
		assignRelations(goal, dto, true);
		return new GoalDTO(goalRepo.save(goal));

	}

	@Transactional
	public GoalDTO patch(Integer id, GoalCreateDTO dto) {
		Goal goal = RepoUtil.findOrThrow(goalRepo.findById(id), Goal.class);
		GoalMapper.patchFromDTO(dto, goal);
		assignRelations(goal, dto, false);
		return new GoalDTO(goalRepo.save(goal));
	}

	@Transactional
	public void delete(Integer id) {
		Goal goal = RepoUtil.findOrThrow(goalRepo.findById(id), Goal.class);
		DeleteUtil.detachSingle(goal, Goal::getMatch, (match, g) -> match.getGoals().remove(g), matchRepo);
		DeleteUtil.detachSingle(goal, Goal::getScorer, (scorer, g) -> scorer.getGoals().remove(g), playerRepo);
		DeleteUtil.detachSingle(goal, Goal::getAssistant, (assistant, g) -> assistant.getAssists().remove(g), playerRepo);
		goal.setMatch(null);
		goal.setScorer(null);
		goal.setAssistant(null);
		goalRepo.delete(goal);
	}

	private void assignRelations(Goal goal, GoalCreateDTO dto, boolean evenIfNull) {
		if (evenIfNull) {
			DtoMappingUtil.assignEvenIfNull(matchRepo, dto.getMatchId(), goal::setMatch, Match.class);
			DtoMappingUtil.assignEvenIfNull(playerRepo, dto.getScorerId(), goal::setScorer, Player.class);
			DtoMappingUtil.assignEvenIfNull(playerRepo, dto.getAssistantId(), goal::setAssistant, Player.class);
		} else {
			DtoMappingUtil.assignIfNotNull(matchRepo, dto.getMatchId(), goal::setMatch, Match.class);
			DtoMappingUtil.assignIfNotNull(playerRepo, dto.getScorerId(), goal::setScorer, Player.class);
			DtoMappingUtil.assignIfNotNull(playerRepo, dto.getAssistantId(), goal::setAssistant, Player.class);
		}
	}
}
