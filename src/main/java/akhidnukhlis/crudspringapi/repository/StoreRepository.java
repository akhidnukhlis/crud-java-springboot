package akhidnukhlis.crudspringapi.repository;

import akhidnukhlis.crudspringapi.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {

//    Optional<Address> findFirstByStoreAndId(Store store, String id);
//
//    List<Address> findAllByStore(Store store);
}
