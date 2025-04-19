package pl.polsl.FootballLeague.util;

import static pl.polsl.FootballLeague.util.RepoUtil.findOrThrow;

import java.util.function.Consumer;

import org.springframework.data.repository.CrudRepository;

public class DtoMappingUtil {
	public static <T> void copyIfNotNull(T value, Consumer<T> setter) {
		if (value != null) {
			setter.accept(value);
		}
	}

	public static <T> void assignIfNotNull(CrudRepository<T, Integer> repo, Integer id, Consumer<T> setter,
			Class<?> clazz) {
		if (id != null) {
			T found = findOrThrow(repo.findById(id), clazz);
			setter.accept(found);
		}
	}

	public static <T> void assignEvenIfNull(CrudRepository<T, Integer> repo, Integer id, Consumer<T> setter,
			Class<?> clazz) {
		setter.accept(id != null ? findOrThrow(repo.findById(id), clazz) : null);
	}
}
