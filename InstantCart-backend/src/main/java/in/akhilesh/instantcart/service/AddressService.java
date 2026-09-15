package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.address.AddressRequest;
import in.akhilesh.instantcart.dto.address.AddressResponse;
import in.akhilesh.instantcart.entity.Address;
import in.akhilesh.instantcart.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    @PreAuthorize(value = "hasRole('CUSTOMER') and #userId == authentication.principal.userId")
    public List<AddressResponse> fetchAddress(ObjectId userId) {

        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    @PreAuthorize(value = "hasRole('CUSTOMER') and #userId == authentication.principal.userId")
    public AddressResponse createAddress(ObjectId userId, AddressRequest request) {

        Address address = mapRequestToAddress(userId,request);
        Address saved = addressRepository.save(address);

        return mapToResponse(saved);
    }


    @Transactional
    @PreAuthorize(value = "hasRole('CUSTOMER') and #userId == authentication.principal.userId")
    public AddressResponse updateAddress(ObjectId addressId, ObjectId userId, AddressRequest request) {

        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setLabel(request.getLabel());
        address.setAddress(request.getAddress());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZip(request.getZip());
        address.setIsDefault(request.getIsDefault());
        address.setLat(request.getLat());
        address.setLng(request.getLng());

        return mapToResponse(addressRepository.save(address));
    }

    @Transactional
    @PreAuthorize(value = "hasRole('CUSTOMER') and #userId == authentication.principal.userId")
    public void deleteAddress(ObjectId addressId, ObjectId userId) {

        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        addressRepository.delete(address);
    }

    private AddressResponse mapToResponse(Address address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId().toHexString());
        response.setUserId(address.getUserId().toHexString());
        response.setLabel(address.getLabel());
        response.setAddress(address.getAddress());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setZip(address.getZip());
        response.setIsDefault(address.getIsDefault());
        response.setLat(address.getLat());
        response.setLng(address.getLng());
        response.setCreatedAt(address.getCreatedAt());
        response.setUpdatedAt(address.getUpdatedAt());
        return response;
    }


    private Address mapRequestToAddress(ObjectId userId,AddressRequest request) {
        Address address = new Address();
        address.setUserId(userId);
        address.setLabel(request.getLabel());
        address.setAddress(request.getAddress());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZip(request.getZip());
        address.setIsDefault(request.getIsDefault());
        address.setLat(request.getLat());
        address.setLng(request.getLng());
        return address;
    }


}