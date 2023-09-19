package akhidnukhlis.crudspringapi.controller;

import akhidnukhlis.crudspringapi.model.*;
import akhidnukhlis.crudspringapi.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandController {
    @Autowired
    BrandService brandService;

    @PostMapping(
            path = "/api/brands",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<BrandResponse> create(@RequestBody CreateBrandRequest request) {

        BrandResponse brandResponse = brandService.create(request);
        return WebResponse.<BrandResponse>builder().data(brandResponse).build();
    }
}
