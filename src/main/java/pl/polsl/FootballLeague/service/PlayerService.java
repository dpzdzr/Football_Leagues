package pl.polsl.FootballLeague.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pl.polsl.FootballLeague.dto.input.PlayerCreateDTO;
import pl.polsl.FootballLeague.dto.output.ClubDTO;
import pl.polsl.FootballLeague.dto.output.GoalDTO;
import pl.polsl.FootballLeague.dto.output.PlayerDTO;
import pl.polsl.FootballLeague.dto.output.PositionDTO;
import pl.polsl.FootballLeague.mapper.PlayerMapper;
import pl.polsl.FootballLeague.model.Club;
import pl.polsl.FootballLeague.model.Player;
import pl.polsl.FootballLeague.model.Position;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.GoalRepository;
import pl.polsl.FootballLeague.repository.PlayerRepository;
import pl.polsl.FootballLeague.repository.PositionRepository;
import pl.polsl.FootballLeague.util.DeleteUtil;
import pl.polsl.FootballLeague.util.DtoMappingUtil;
import pl.polsl.FootballLeague.util.RepoUtil;

@Service
@RequiredArgsConstructor
public class PlayerService {
	private final PlayerRepository playerRepo;
	private final ClubRepository clubRepo;
	private final PositionRepository positionRepo;
	private final GoalRepository goalRepo;

	public List<PlayerDTO> getAll() {
		return StreamSupport.stream(playerRepo.findAll().spliterator(), false).map(PlayerDTO::new).toList();
	}

	public PlayerDTO getById(Integer id) {
		return new PlayerDTO(findPlayer(id));
	}

	public ClubDTO getClub(Integer id) {
		return Optional.of(findPlayer(id).getClub()).map(ClubDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not assigned"));
	}

	public PositionDTO getPosition(Integer id) {
		return Optional.of(findPlayer(id).getPosition()).map(PositionDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not assigned"));
	}

	public List<GoalDTO> getGoals(Integer id) {
		return findPlayer(id).getGoals().stream().map(GoalDTO::new).toList();
	}

	public List<GoalDTO> getAssists(Integer id) {
		return findPlayer(id).getAssists().stream().map(GoalDTO::new).toList();
	}

	@Transactional
	public PlayerDTO create(PlayerCreateDTO dto) {
		Player player = new Player();
		PlayerMapper.updateFromDto(dto, player);
		assignRelations(dto, player, true);
		return new PlayerDTO(playerRepo.save(player));
	}
	
	@Transactional
	public PlayerDTO update(Integer id, PlayerCreateDTO dto) {
		Player player = findPlayer(id);
		PlayerMapper.updateFromDto(dto, player);
		assignRelations(dto, player, true);
		return new PlayerDTO(playerRepo.save(player));
	}
	
	@Transactional
	public PlayerDTO patch(Integer id, PlayerCreateDTO dto) {
		Player player = findPlayer(id);
		PlayerMapper.patchFromDto(dto, player);
		assignRelations(dto, player, false);
		return new PlayerDTO(playerRepo.save(player));
	}
	
	@Transactional
	public void delete(Integer id) {
		Player player = findPlayer(id);
		DeleteUtil.detachCollection(player, Player::getGoals, g -> g.setScorer(null), goalRepo);
		DeleteUtil.detachCollection(player, Player::getAssists, g -> g.setAssistant(null), goalRepo);
		playerRepo.delete(player);
	}
	
	private Player findPlayer(Integer id) {
		return RepoUtil.findOrThrow(playerRepo.findById(id), Player.class);
	}

	private void assignRelations(PlayerCreateDTO dto, Player player, boolean evenIfNull) {
		if (evenIfNull) {
			DtoMappingUtil.assignEvenIfNull(clubRepo, dto.getClubId(), player::setClub, Club.class);
			DtoMappingUtil.assignEvenIfNull(positionRepo, dto.getPositionId(), player::setPosition, Position.class);
		} else {
			DtoMappingUtil.assignIfNotNull(clubRepo, dto.getClubId(), player::setClub, Club.class);
			DtoMappingUtil.assignIfNotNull(positionRepo, dto.getPositionId(), player::setPosition, Position.class);
		}
	}
}
