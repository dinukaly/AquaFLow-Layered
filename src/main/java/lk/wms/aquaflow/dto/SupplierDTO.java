package lk.wms.aquaflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupplierDTO {
    private String supplierId;
    private String name;
    private String address;
    private String email;
    private String tel;
}

