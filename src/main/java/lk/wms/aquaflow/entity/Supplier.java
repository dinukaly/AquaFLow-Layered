package lk.wms.aquaflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    private String supplierId;
    private String name;
    private String address;
    private String email;
    private String tel;
}
