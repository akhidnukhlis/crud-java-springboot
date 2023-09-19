package akhidnukhlis.crudspringapi.repository;

import akhidnukhlis.crudspringapi.entity.Brand;
import akhidnukhlis.crudspringapi.entity.Contact;
import akhidnukhlis.crudspringapi.entity.Store;
import akhidnukhlis.crudspringapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String>, JpaSpecificationExecutor<Brand> {
    Optional<Brand> findFirstById(String id);
}
