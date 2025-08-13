package lk.wms.aquaflow.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OfficerDTO {
    private String officerId;
    private String name;
    private String address;
    private String email;
    private String telephone;

    @Override
    public String toString() {
        return name + " (" + officerId + ")";
    }

}

