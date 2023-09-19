package akhidnukhlis.crudspringapi.service;

import akhidnukhlis.crudspringapi.entity.Brand;
import akhidnukhlis.crudspringapi.entity.Store;
import akhidnukhlis.crudspringapi.model.*;
import akhidnukhlis.crudspringapi.repository.BrandRepository;
import jakarta.persistence.criteria.Predicate;
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

    @Transactional(readOnly = true)
    public BrandResponse get(String id) {
        Brand brand = brandRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));

        return toBrandResponse(brand);
    }

    @Transactional
    public BrandResponse update(UpdateBrandRequest request) {
        validationService.validate(request);

        Brand brand = brandRepository.findFirstById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));

        brand.setBrandName(request.getBrandName());

        brandRepository.save(brand);

        return toBrandResponse(brand);
    }

    @Transactional
    public void delete(String storeId) {
        Brand brand = brandRepository.findFirstById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand not found"));

        brandRepository.delete(brand);
    }

    @Transactional(readOnly = true)
    public Page<BrandResponse> search(SearchBrandRequest request) {
        Specification<Brand> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(request.getBrandName())) {
                predicates.add(builder.or(
                        builder.like(root.get("brandName"), "%" + request.getBrandName() + "%")
                ));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Brand> brands = brandRepository.findAll(specification, pageable);
        List<BrandResponse> brandResponses = brands.getContent().stream()
                .map(this::toBrandResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(brandResponses, pageable, brands.getTotalElements());
    }

    private BrandResponse toBrandResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .build();
    }
}
