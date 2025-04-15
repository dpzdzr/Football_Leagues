package pl.polsl.FootballLeague;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.data.repository.CrudRepository;

public class DeleteUtil {
	public static <T, U> void removeAssociation(CrudRepository<T, Integer> repo, U object, Function<U, T> getter,
			BiConsumer<T, U> remover) {
		T associationObject = getter.apply(object);
		if (associationObject != null) {
			remover.accept(associationObject, object);
			repo.save(associationObject);
		}
	}

	public static <T, U> List<U> detach(T parent, Function<T, List<U>> getter, Consumer<U> nullifier) {
		List<U> list = Optional.ofNullable(getter.apply(parent)).orElse(List.of());
		return list.stream().peek(nullifier).toList();
	}
}
