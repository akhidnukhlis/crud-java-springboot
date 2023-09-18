package akhidnukhlis.crudspringapi.controller;

import akhidnukhlis.crudspringapi.entity.User;
import akhidnukhlis.crudspringapi.model.CreateStoreRequest;
import akhidnukhlis.crudspringapi.model.StoreResponse;
import akhidnukhlis.crudspringapi.model.WebResponse;
import akhidnukhlis.crudspringapi.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping(
            path = "/api/store",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<StoreResponse> create(User user, @RequestBody CreateStoreRequest request) {

        StoreResponse storeResponse = storeService.create(user, request);
        return WebResponse.<StoreResponse>builder().data(storeResponse).build();
    }
}
