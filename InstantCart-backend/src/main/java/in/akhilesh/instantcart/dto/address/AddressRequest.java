package in.akhilesh.instantcart.dto.address;

import lombok.Data;

@Data
public class AddressRequest {
    private String label;
    private String address;
    private String city;
    private String state;
    private String zip;
    private Boolean isDefault;
    private Double lat;
    private Double lng;
}
