package cz.cez.cpr.eacodegenerator.core.repository;

import cz.cez.cpr.eacodegenerator.core.metamodel.TPackage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TPackageRepository extends CrudRepository<TPackage, Long> {

	List<TPackage> findTPackageByParentId(Long parentId);

}
