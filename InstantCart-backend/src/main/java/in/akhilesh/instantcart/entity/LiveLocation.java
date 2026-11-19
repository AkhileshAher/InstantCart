package in.akhilesh.instantcart.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class LiveLocation {

    private Double lat;

    private Double lng;
}