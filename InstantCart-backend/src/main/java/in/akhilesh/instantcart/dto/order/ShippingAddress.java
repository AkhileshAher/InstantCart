package in.akhilesh.instantcart.dto.order;

import lombok.Data;

@Data
public class ShippingAddress {

    private String label;

    private String address;

    private String city;

    private String state;

    private String zip;

    private Double lat;

    private Double lng;
}