package pl.polsl.FootballLeague.util;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.data.repository.CrudRepository;

public class DeleteUtil {
	public static <P, C> void detachSingle(P parent, Function<P, C> getter,
			BiConsumer<C, P> remover, CrudRepository<C, Integer> repo) {
		C associationObject = getter.apply(parent);
		if (associationObject != null) {
			remover.accept(associationObject, parent);
			repo.save(associationObject);
		}
	}
	
	public static <P, C> void detachSingle(P parent, Function<P, C> getter,
			Consumer<C> remover, CrudRepository<C, Integer> repo) {
		detachSingle(parent, getter, (c, p) ->remover.accept(c), repo);
	}

	public static <P, C> void detachCollection(P parent, Function<P, List<C>> getter, Consumer<C> nullifier,
			CrudRepository<C, Integer> repo) {
		List<C> list = Optional.ofNullable(getter.apply(parent)).orElse(List.of());
		if (!list.isEmpty()) {
			list.forEach(nullifier);
			repo.saveAll(list);
		}
	}
}
