package pl.polsl.FootballLeague.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pl.polsl.FootballLeague.dto.input.StadiumCreateDTO;
import pl.polsl.FootballLeague.dto.output.ClubDTO;
import pl.polsl.FootballLeague.dto.output.StadiumDTO;
import pl.polsl.FootballLeague.mapper.StadiumMapper;
import pl.polsl.FootballLeague.model.Stadium;
import pl.polsl.FootballLeague.repository.ClubRepository;
import pl.polsl.FootballLeague.repository.StadiumRepository;
import pl.polsl.FootballLeague.util.DeleteUtil;
import pl.polsl.FootballLeague.util.RepoUtil;

@Service
@RequiredArgsConstructor
public class StadiumService {
	private final StadiumRepository stadiumRepo;
	private final ClubRepository clubRepo;

	public List<StadiumDTO> getAll() {
		return StreamSupport.stream(stadiumRepo.findAll().spliterator(), false).map(StadiumDTO::new).toList();
	}

	public StadiumDTO getById(Integer id) {
		return new StadiumDTO(findStadium(id));
	}

	public ClubDTO getClub(Integer id) {
		Stadium stadium = findStadium(id);
		return Optional.ofNullable(stadium.getClub()).map(ClubDTO::new)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not assigned"));
	}

	@Transactional
	public StadiumDTO create(StadiumCreateDTO dto) {
		Stadium stadium = new Stadium();
		StadiumMapper.updateFromDto(dto, stadium);
		return new StadiumDTO(stadiumRepo.save(stadium));
	}

	@Transactional
	public StadiumDTO update(Integer id, StadiumCreateDTO dto) {
		Stadium stadium = findStadium(id);
		StadiumMapper.updateFromDto(dto, stadium);
		return new StadiumDTO(stadiumRepo.save(stadium));
	}

	@Transactional
	public StadiumDTO patch(Integer id, StadiumCreateDTO dto) {
		Stadium stadium = findStadium(id);
		StadiumMapper.patchFromDto(dto, stadium);
		return new StadiumDTO(stadiumRepo.save(stadium));
	}

	@Transactional
	public void delete(Integer id) {
		Stadium stadium = findStadium(id);
		DeleteUtil.detachSingle(stadium, Stadium::getClub, c -> c.setStadium(null), clubRepo);
		stadiumRepo.delete(stadium);
	}

	private Stadium findStadium(Integer id) {
		return RepoUtil.findOrThrow(stadiumRepo.findById(id), Stadium.class);
	}
}
