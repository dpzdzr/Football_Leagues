package pl.polsl.FootballLeague.util;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RepoUtil {
	public static <T> T findOrThrow(Optional<T> optional, Class<?> clazz) {
		return optional.orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, clazz.getSimpleName() + " not found"));
	}
}
