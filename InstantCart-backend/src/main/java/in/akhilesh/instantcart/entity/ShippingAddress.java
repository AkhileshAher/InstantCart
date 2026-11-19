package in.akhilesh.instantcart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ShippingAddress {

    @Column(name = "ship_name")
    private String name;

    @Column(name = "ship_phone")
    private String phone;

    @Column(name = "ship_address")
    private String address;

    @Column(name = "ship_city")
    private String city;

    @Column(name = "ship_state")
    private String state;

    @Column(name = "ship_zip")
    private String zip;

    @Column(name = "ship_lat")
    private Double lat;

    @Column(name = "ship_lng")
    private Double lng;
}