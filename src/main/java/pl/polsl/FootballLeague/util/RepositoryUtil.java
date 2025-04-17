package pl.polsl.FootballLeague.util;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RepositoryUtil {
	public static <T> T findOrThrow(Optional<T> optional, String entityName) {
		return optional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entityName + " not found"));
	}

	public static void existsOrThrow(boolean exists, String entityName) {
		if (!exists)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, entityName + " not found");
	}
}
