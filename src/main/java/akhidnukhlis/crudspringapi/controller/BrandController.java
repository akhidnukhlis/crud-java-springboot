package akhidnukhlis.crudspringapi.controller;

import akhidnukhlis.crudspringapi.model.*;
import akhidnukhlis.crudspringapi.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(
            path = "/api/brands/{brandId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<BrandResponse> get(@PathVariable("brandId") String brandId) {
        BrandResponse brandResponse = brandService.get(brandId);
        return WebResponse.<BrandResponse>builder().data(brandResponse).build();
    }

    @PutMapping(
            path = "api/brands/{brandId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<BrandResponse> update(@RequestBody UpdateBrandRequest request,
                                             @PathVariable("brandId") String brandId) {
        request.setId(brandId);

        BrandResponse brandResponse = brandService.update(request);
        return WebResponse.<BrandResponse>builder().data(brandResponse).build();
    }

    @DeleteMapping(
            path = "/api/brands/{brandId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(@PathVariable("brandId") String brandId) {
        brandService.delete(brandId);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/brands",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<BrandResponse>> search(@RequestParam(value = "brand_name", required = false) String brandName,
                                                   @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        SearchBrandRequest request = SearchBrandRequest.builder()
                .page(page)
                .size(size)
                .brandName(brandName)
                .build();

        Page<BrandResponse> brandResponses = brandService.search(request);
        return WebResponse.<List<BrandResponse>>builder()
                .data(brandResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(brandResponses.getNumber())
                        .totalPage(brandResponses.getTotalPages())
                        .size(brandResponses.getSize())
                        .build())
                .build();
    }
}
