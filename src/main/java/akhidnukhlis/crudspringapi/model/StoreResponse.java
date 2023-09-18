package akhidnukhlis.crudspringapi.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponse {

    private  String id;

    private String storeName;

    private String phone;

    private String email;

    private String street;

    private String city;

    private String state;

    private String zipCode;
}
