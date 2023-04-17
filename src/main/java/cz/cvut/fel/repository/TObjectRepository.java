package cz.cvut.fel.repository;

import cz.cvut.fel.metamodel.TObject;
import cz.cvut.fel.metamodel.TPackage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface TObjectRepository extends CrudRepository<TObject, Long> {

	Stream<TObject> findByTypeAndName(String type, String name);

	Optional<TObject> findByNameAndEaGuid(String name, String eaGuid);

	List<TObject> findByTpackageAndType(TPackage tPackage, String type);

	List<TObject> findByName(String name);
}
