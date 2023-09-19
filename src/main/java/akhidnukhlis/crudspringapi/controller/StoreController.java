package akhidnukhlis.crudspringapi.controller;

import akhidnukhlis.crudspringapi.entity.User;
import akhidnukhlis.crudspringapi.model.*;
import akhidnukhlis.crudspringapi.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping(
            path = "/api/stores",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<StoreResponse> create(@RequestBody CreateStoreRequest request) {

        StoreResponse storeResponse = storeService.create(request);
        return WebResponse.<StoreResponse>builder().data(storeResponse).build();
    }

    @GetMapping(
            path = "/api/stores/{storeId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<StoreResponse> get(@PathVariable("storeId") String storeId) {
        StoreResponse storeResponse = storeService.get(storeId);
        return WebResponse.<StoreResponse>builder().data(storeResponse).build();
    }

    @PutMapping(
            path = "api/stores/{storeId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<StoreResponse> update(@RequestBody UpdateStoreRequest request,
                                             @PathVariable("storeId") String storeID) {
        request.setId(storeID);

        StoreResponse storeResponse = storeService.update(request);
        return WebResponse.<StoreResponse>builder().data(storeResponse).build();
    }

    @DeleteMapping(
            path = "/api/stores/{storeId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(@PathVariable("storeId") String storeId) {
        storeService.delete(storeId);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/stores",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<StoreResponse>> search(@RequestParam(value = "name_store", required = false) String storeName,
                                                   @RequestParam(value = "email", required = false) String email,
                                                   @RequestParam(value = "phone", required = false) String phone,
                                                   @RequestParam(value = "city", required = false) String city,
                                                   @RequestParam(value = "state", required = false) String state,
                                                   @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        SearchStoreRequest request = SearchStoreRequest.builder()
                .page(page)
                .size(size)
                .storeName(storeName)
                .email(email)
                .phone(phone)
                .city(city)
                .state(state)
                .build();

        Page<StoreResponse> storeResponses = storeService.search(request);
        return WebResponse.<List<StoreResponse>>builder()
                .data(storeResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(storeResponses.getNumber())
                        .totalPage(storeResponses.getTotalPages())
                        .size(storeResponses.getSize())
                        .build())
                .build();
    }
}
