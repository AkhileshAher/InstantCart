package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.address.AddressRequest;
import in.akhilesh.instantcart.dto.address.AddressResponse;
import in.akhilesh.instantcart.security.JwtPrincipal;
import in.akhilesh.instantcart.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddresses(Authentication authentication) {
        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        ObjectId userId = principal.getUserId();
        return ResponseEntity.ok(addressService.fetchAddress(userId));
    }

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(
            Authentication authentication, @RequestBody AddressRequest request
    ) {
        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        ObjectId userId = principal.getUserId();

        AddressResponse response = addressService.createAddress(userId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable ObjectId id,
            Authentication authentication,
            @RequestBody AddressRequest request
    ) {

        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        ObjectId userId = principal.getUserId();

        return ResponseEntity.ok(addressService.updateAddress(
                        id,
                        userId,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable ObjectId id,
            Authentication authentication
    ) {

        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        ObjectId userId = principal.getUserId();

        addressService.deleteAddress(id, userId);
        return ResponseEntity.noContent().build();
    }
}