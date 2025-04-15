package pl.polsl.FootballLeague;

import static pl.polsl.FootballLeague.ExceptionUtil.findOrThrow;

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.data.repository.CrudRepository;

public class PatchUtil {
	public static <T> void copyIfNotNull(T value, Consumer<T> setter) {
		if (value != null) {
			setter.accept(value);
		}
	}

	public static <T> void copyIfExists(CrudRepository<T, Integer> repo, T object, Function<T, Integer> idGetter,
			Consumer<T> setter, String message) {
		if (object != null && idGetter.apply(object) != null) {
			object = findOrThrow(repo.findById(idGetter.apply(object)), message);
			setter.accept(object);
		}
	}
}
