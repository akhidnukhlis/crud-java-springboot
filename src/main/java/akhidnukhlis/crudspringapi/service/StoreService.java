package akhidnukhlis.crudspringapi.service;

import akhidnukhlis.crudspringapi.entity.Address;
import akhidnukhlis.crudspringapi.entity.Contact;
import akhidnukhlis.crudspringapi.entity.Store;
import akhidnukhlis.crudspringapi.entity.User;
import akhidnukhlis.crudspringapi.model.*;
import akhidnukhlis.crudspringapi.repository.StoreRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public StoreResponse create(User user, CreateStoreRequest request) {
        validationService.validate(request);

        Store store = new Store();
        store.setId(UUID.randomUUID().toString());
        store.setStoreName(request.getStoreName());
        store.setPhone(request.getPhone());
        store.setEmail(request.getEmail());
        store.setStreet(request.getStreet());
        store.setCity(request.getCity());
        store.setState(request.getState());
        store.setZipCode(request.getZipCode());
        store.setUser(user);

        storeRepository.save(store);

        return toStoreResponse(store);
    }

    @Transactional(readOnly = true)
    public StoreResponse get(User user, String id) {
        Store store = storeRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found"));

        return toStoreResponse(store);
    }

    @Transactional
    public StoreResponse update(User user, UpdateStoreRequest request) {
        validationService.validate(request);

        Store store = storeRepository.findFirstByUserAndId(user, request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found"));

        store.setStoreName(request.getStoreName());
        store.setPhone(request.getPhone());
        store.setEmail(request.getEmail());
        store.setStreet(request.getStreet());
        store.setCity(request.getCity());
        store.setState(request.getState());
        store.setZipCode(request.getZipCode());

        storeRepository.save(store);

        return toStoreResponse(store);
    }

    @Transactional
    public void delete(User user, String storeId) {
        Store store = storeRepository.findFirstByUserAndId(user, storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found"));

        storeRepository.delete(store);
    }

    @Transactional(readOnly = true)
    public Page<StoreResponse> search(User user, SearchStoreRequest request) {
        Specification<Store> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));
            if (Objects.nonNull(request.getStoreName())) {
                predicates.add(builder.or(
                        builder.like(root.get("storeName"), "%" + request.getStoreName() + "%")
                ));
            }
            if (Objects.nonNull(request.getEmail())) {
                predicates.add(builder.like(root.get("email"), "%" + request.getEmail() + "%"));
            }
            if (Objects.nonNull(request.getPhone())) {
                predicates.add(builder.like(root.get("phone"), "%" + request.getPhone() + "%"));
            }
            if (Objects.nonNull(request.getCity())) {
                predicates.add(builder.like(root.get("city"), "%" + request.getCity() + "%"));
            }
            if (Objects.nonNull(request.getState())) {
                predicates.add(builder.like(root.get("state"), "%" + request.getState() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Store> stores = storeRepository.findAll(specification, pageable);
        List<StoreResponse> storeResponses = stores.getContent().stream()
                .map(this::toStoreResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(storeResponses, pageable, stores.getTotalElements());
    }

    private StoreResponse toStoreResponse(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .storeName(store.getStoreName())
                .phone(store.getPhone())
                .email(store.getEmail())
                .street(store.getStreet())
                .city(store.getCity())
                .state(store.getState())
                .zipCode(store.getZipCode())
                .build();
    }
}
