package akhidnukhlis.crudspringapi.controller;

import akhidnukhlis.crudspringapi.entity.User;
import akhidnukhlis.crudspringapi.model.CreateStoreRequest;
import akhidnukhlis.crudspringapi.model.StoreResponse;
import akhidnukhlis.crudspringapi.model.UpdateStoreRequest;
import akhidnukhlis.crudspringapi.model.WebResponse;
import akhidnukhlis.crudspringapi.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping(
            path = "/api/stores",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<StoreResponse> create(User user, @RequestBody CreateStoreRequest request) {

        StoreResponse storeResponse = storeService.create(user, request);
        return WebResponse.<StoreResponse>builder().data(storeResponse).build();
    }

    @GetMapping(
            path = "/api/stores/{storeId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<StoreResponse> get(User user, @PathVariable("storeId") String storeId) {
        StoreResponse storeResponse = storeService.get(user, storeId);
        return WebResponse.<StoreResponse>builder().data(storeResponse).build();
    }

    @PutMapping(
            path = "api/stores/{storeId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<StoreResponse> update(User user,
                                             @RequestBody UpdateStoreRequest request,
                                             @PathVariable("storeId") String storeID) {
        request.setId(storeID);

        StoreResponse storeResponse = storeService.update(user, request);
        return WebResponse.<StoreResponse>builder().data(storeResponse).build();
    }
}
