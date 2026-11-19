package in.akhilesh.instantcart.dto;

import in.akhilesh.instantcart.entity.Address;

public record AddressResponse(
            Long id, String label, String addressLine, String city, String state,
            String zip, Double lat, Double lng, Boolean isDefault) {

        public static AddressResponse from(Address a) {
            return new AddressResponse(a.getId(), a.getLabel(), a.getAddressLine(), a.getCity(),
                    a.getState(), a.getZip(), a.getLat(), a.getLng(), a.getIsDefault());
        }
    }
