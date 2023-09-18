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
public class SearchStoreRequest {

    private String storeName;

    private String email;

    private String phone;

    private String city;

    private String state;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
