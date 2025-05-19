package pl.polsl.FootballLeague;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import pl.polsl.FootballLeague.dto.input.*;
import pl.polsl.FootballLeague.service.*;

@Component
@RequiredArgsConstructor
public class TestDataInitializer implements CommandLineRunner {
	
	private final LeagueService leagueService;
	private final StadiumService stadiumService;
	private final PositionService positionService;
	private final ClubService clubService;
	private final PlayerService playerService;
	private final MatchService matchService;
	private final GoalService goalService;

	@Override
	public void run(String... args) throws Exception {

		initializeLeagues();
		initializeStadiums();
		initializePositions();
		initializeClubs();
		initializePlayers();
		initializeMatch();
		initializeGoal();

	}
	
	private void initializeLeagues() {
		if(leagueService.getAll().isEmpty()) {
			LeagueCreateDTO premierLeague = new LeagueCreateDTO(); 
			premierLeague.setName("Premier League");
			premierLeague.setCountry("England");
		
			leagueService.create(premierLeague);
		}
	}
	
	private void initializeStadiums() {
		if(stadiumService.getAll().isEmpty()) {
			StadiumCreateDTO wembley = new StadiumCreateDTO();
			wembley.setName("Wembley Stadium");
			wembley.setCapacity(90000);
			wembley.setAddress("London, England");
			
			StadiumCreateDTO trafford = new StadiumCreateDTO();
			trafford.setName("Old Trafford");
			trafford.setCapacity(75000);
			trafford.setAddress("Manchester, England");
			
			StadiumCreateDTO anfield = new StadiumCreateDTO();
			anfield.setName("Anfield");
			anfield.setCapacity(61000);
			anfield.setAddress("Liverpool, England");
			
			stadiumService.create(wembley);
			stadiumService.create(trafford);
			stadiumService.create(anfield);
		}
	}
	
	private void initializePositions() {
		if(positionService.getAll().isEmpty()) {
			PositionCreateDTO attacker = new PositionCreateDTO();
			attacker.setName("Attacker");
			
			PositionCreateDTO defender = new PositionCreateDTO();
			defender.setName("Defender");
			
			positionService.create(attacker);
			positionService.create(defender);
		}
	}
	
	private void initializeClubs() {
		if(clubService.getAll().isEmpty()) {
			ClubCreateDTO arsenal = new ClubCreateDTO();
			arsenal.setCity("Holloway");
			arsenal.setFoundationYear(1904);
			arsenal.setLeagueId(1);
			arsenal.setName("Arsenal");
			arsenal.setPoints(0);
			arsenal.setStadiumId(1);
			
			ClubCreateDTO manchesterUnited = new ClubCreateDTO();
			manchesterUnited.setCity("Manchester");
			manchesterUnited.setFoundationYear(1878);
			manchesterUnited.setLeagueId(1);
			manchesterUnited.setName("Manchester United");
			manchesterUnited.setPoints(0);
			manchesterUnited.setStadiumId(2);
			
			clubService.create(arsenal);
			clubService.create(manchesterUnited);
		}
	}
	
	private void initializePlayers() {
		if(playerService.getAll().isEmpty()) {
			PlayerCreateDTO fredricson = new PlayerCreateDTO();
			fredricson.setBirthdayDate(LocalDate.of(1990, 1, 1));
			fredricson.setClubId(2);
			fredricson.setFirstName("Tyler");
			fredricson.setLastName("Fredricson");
			fredricson.setPositionId(1);
			
			PlayerCreateDTO harrison = new PlayerCreateDTO();
			harrison.setBirthdayDate(LocalDate.of(1990, 1, 1));
			harrison.setClubId(2);
			harrison.setFirstName("Elyh");
			harrison.setLastName("Harrison");
			harrison.setPositionId(2);
			
			PlayerCreateDTO kuczynski = new PlayerCreateDTO();
			kuczynski.setBirthdayDate(LocalDate.of(1990, 1, 1));
			kuczynski.setClubId(1);
			kuczynski.setFirstName("Max");
			kuczynski.setLastName("Kuczynski");
			kuczynski.setPositionId(1);
			
			PlayerCreateDTO oyetunde = new PlayerCreateDTO();
			oyetunde.setBirthdayDate(LocalDate.of(1990, 1, 1));
			oyetunde.setClubId(1);
			oyetunde.setFirstName("Daniel");
			oyetunde.setLastName("Oyetunde");
			oyetunde.setPositionId(2);
			
			playerService.create(fredricson);
			playerService.create(harrison);
			playerService.create(kuczynski);
			playerService.create(oyetunde);
		}
	}
	
	private void initializeMatch() {
		if(matchService.getAll().isEmpty()) {
			MatchCreateDTO friendlyMatch = new MatchCreateDTO();
			friendlyMatch.setAwayClubId(1);
			friendlyMatch.setAwayScore(1);
			friendlyMatch.setDateTime(LocalDateTime.of(LocalDate.of(2025, 5, 15), LocalTime.of(15, 30)));
			friendlyMatch.setHomeClubId(2);
			friendlyMatch.setHomeScore(2);
			
			matchService.create(friendlyMatch);
		}
	}
	
	private void initializeGoal() {
		if(goalService.getAll().isEmpty()) {
			GoalCreateDTO firstGoal = new GoalCreateDTO(); 
			firstGoal.setAssistantId(2);
			firstGoal.setMatchId(1);
			firstGoal.setMinuteScored(15);
			firstGoal.setScorerId(1);
			
			GoalCreateDTO secondGoal = new GoalCreateDTO(); 
			secondGoal.setAssistantId(4);
			secondGoal.setMatchId(1);
			secondGoal.setMinuteScored(30);
			secondGoal.setScorerId(3);
			
			GoalCreateDTO thirdGoal = new GoalCreateDTO(); 
			thirdGoal.setAssistantId(4);
			thirdGoal.setMatchId(1);
			thirdGoal.setMinuteScored(50);
			thirdGoal.setScorerId(3);
			
			goalService.create(firstGoal);
			goalService.create(secondGoal);
			goalService.create(thirdGoal);
		}
	}
}
