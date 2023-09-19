package akhidnukhlis.crudspringapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchBrandRequest {

    private String brandName;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
