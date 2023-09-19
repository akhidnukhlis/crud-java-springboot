package akhidnukhlis.crudspringapi.service;

import akhidnukhlis.crudspringapi.entity.Brand;
import akhidnukhlis.crudspringapi.entity.Store;
import akhidnukhlis.crudspringapi.model.BrandResponse;
import akhidnukhlis.crudspringapi.model.CreateBrandRequest;
import akhidnukhlis.crudspringapi.model.StoreResponse;
import akhidnukhlis.crudspringapi.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BrandService {
    @Autowired
    BrandRepository brandRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public BrandResponse create(CreateBrandRequest request) {
        validationService.validate(request);

        Brand brand = new Brand();
        brand.setId(UUID.randomUUID().toString());
        brand.setBrandName(request.getBrandName());

        brandRepository.save(brand);

        return toBrandResponse(brand);
    }

    private BrandResponse toBrandResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .build();
    }
}
