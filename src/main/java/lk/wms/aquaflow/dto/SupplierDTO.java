package lk.wms.aquaflow.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierDTO {
    private String supplierId;
    private String name;
    private String address;
    private String email;
    private String tel;
}

