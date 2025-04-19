package pl.polsl.FootballLeague.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pl.polsl.FootballLeague.dto.input.PositionCreateDTO;
import pl.polsl.FootballLeague.dto.output.PlayerDTO;
import pl.polsl.FootballLeague.dto.output.PositionDTO;
import pl.polsl.FootballLeague.mapper.PositionMapper;
import pl.polsl.FootballLeague.model.Position;
import pl.polsl.FootballLeague.repository.PlayerRepository;
import pl.polsl.FootballLeague.repository.PositionRepository;
import pl.polsl.FootballLeague.util.DeleteUtil;
import pl.polsl.FootballLeague.util.RepoUtil;

@Service
@RequiredArgsConstructor
public class PositionService {
	private final PositionRepository positionRepo;
	private final PlayerRepository playerRepo;

	public List<PositionDTO> getAll() {
		return StreamSupport.stream(positionRepo.findAll().spliterator(), false).map(PositionDTO::new).toList();
	}

	public PositionDTO getById(Integer id) {
		return new PositionDTO(findPosition(id));
	}

	public List<PlayerDTO> getPlayers(Integer id) {
		return findPosition(id).getPlayers().stream().map(PlayerDTO::new).toList();
	}

	@Transactional
	public PositionDTO create(PositionCreateDTO dto) {
		Position position = new Position();
		PositionMapper.updateFromDTO(dto, position);
		return new PositionDTO(positionRepo.save(position));
	}

	@Transactional
	public PositionDTO update(Integer id, PositionCreateDTO dto) {
		Position position = findPosition(id);
		PositionMapper.updateFromDTO(dto, position);
		return new PositionDTO(positionRepo.save(position));
	}
	
	@Transactional
	public PositionDTO patch(Integer id, PositionCreateDTO dto) {
		Position position = findPosition(id);
		PositionMapper.patchFromDTO(dto, position);
		return new PositionDTO(positionRepo.save(position));
	}
	
	@Transactional
	public void delete(Integer id) {
		Position position = findPosition(id);
		DeleteUtil.detachCollection(position, Position::getPlayers, p -> p.setPosition(null), playerRepo);
		positionRepo.delete(position);
	}

	private Position findPosition(Integer id) {
		return RepoUtil.findOrThrow(positionRepo.findById(id), Position.class);
	}
}
