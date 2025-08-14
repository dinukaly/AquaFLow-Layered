package lk.wms.aquaflow.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Officer {
    private String officerId;
    private String name;
    private String address;
    private String email;
    private String telephone;
}
