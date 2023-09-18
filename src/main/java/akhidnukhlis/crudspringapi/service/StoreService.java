package akhidnukhlis.crudspringapi.service;

import akhidnukhlis.crudspringapi.entity.Address;
import akhidnukhlis.crudspringapi.entity.Store;
import akhidnukhlis.crudspringapi.entity.User;
import akhidnukhlis.crudspringapi.model.CreateStoreRequest;
import akhidnukhlis.crudspringapi.model.StoreResponse;
import akhidnukhlis.crudspringapi.model.UpdateStoreRequest;
import akhidnukhlis.crudspringapi.model.UpdateUserRequest;
import akhidnukhlis.crudspringapi.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

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
