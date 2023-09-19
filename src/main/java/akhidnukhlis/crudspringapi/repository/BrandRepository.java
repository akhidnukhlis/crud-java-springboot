package akhidnukhlis.crudspringapi.repository;

import akhidnukhlis.crudspringapi.entity.Brand;
import akhidnukhlis.crudspringapi.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {
}
