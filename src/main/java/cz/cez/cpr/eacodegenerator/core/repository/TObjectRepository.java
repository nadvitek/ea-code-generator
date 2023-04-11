package cz.cez.cpr.eacodegenerator.core.repository;

import cz.cez.cpr.eacodegenerator.core.metamodel.TObject;
import cz.cez.cpr.eacodegenerator.core.metamodel.TPackage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface TObjectRepository extends CrudRepository<TObject, Long> {

	Stream<TObject> findByTypeAndName(String type, String name);

	Optional<TObject> findByNameAndEaGuid(String name, String eaGuid);

	List<TObject> findByTpackageAndType(TPackage tPackage, String type);

	List<TObject> findByName(String name);
}
