package pl.polsl.FootballLeague.mapper;

import static pl.polsl.FootballLeague.util.DtoMappingUtil.assignEvenIfNull;
import static pl.polsl.FootballLeague.util.DtoMappingUtil.assignIfNotNull;
import static pl.polsl.FootballLeague.util.DtoMappingUtil.copyIfNotNull;

import pl.polsl.FootballLeague.dto.GoalCreateDTO;
import pl.polsl.FootballLeague.model.Goal;
import pl.polsl.FootballLeague.repository.GoalRepository;
import pl.polsl.FootballLeague.repository.MatchRepository;
import pl.polsl.FootballLeague.repository.PlayerRepository;

public class GoalMapper {
	public static void toEntity(GoalCreateDTO dto, Goal goal, MatchRepository matchRepo, PlayerRepository playerRepo) {
		goal.setMinuteScored(dto.getMinuteScored());
		assignIfNotNull(matchRepo, dto.getMatchId(), goal::setMatch, "Match");
		assignIfNotNull(playerRepo, dto.getScorerId(), goal::setScorer, "Player (scorer)");
		assignIfNotNull(playerRepo, dto.getAssistantId(), goal::setAssistant, "Player (assistant)");
	}

	public static void put(GoalCreateDTO dto, Goal goal, GoalRepository goalRepo, MatchRepository matchRepo,
			PlayerRepository playerRepo) {
		goal.setMinuteScored(dto.getMinuteScored());
		assignEvenIfNull(matchRepo, dto.getMatchId(), goal::setMatch, "Match");
		assignEvenIfNull(playerRepo, dto.getScorerId(), goal::setScorer, "Player (scorer)");
		assignEvenIfNull(playerRepo, dto.getAssistantId(), goal::setAssistant, "Player (assistant)");
	}

	public static void patch(GoalCreateDTO dto, Goal goal, GoalRepository goalRepo, MatchRepository matchRepo,
			PlayerRepository playerRepo) {
		copyIfNotNull(dto.getMinuteScored(), goal::setMinuteScored);
		assignIfNotNull(matchRepo, dto.getMatchId(), goal::setMatch, "Match");
		assignIfNotNull(playerRepo, dto.getScorerId(), goal::setScorer, "Player (scorer)");
		assignIfNotNull(playerRepo, dto.getAssistantId(), goal::setAssistant, "Player (assistant)");
	}
}
