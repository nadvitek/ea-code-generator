package cz.cvut.fel.repository;

import cz.cvut.fel.metamodel.TPackage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This is a repository for TPackage entity
 */
@Repository
public interface TPackageRepository extends CrudRepository<TPackage, Long> {

	List<TPackage> findTPackageByParentId(Long parentId);

}
