package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.AddressRequest;
import in.akhilesh.instantcart.dto.AddressResponse;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.service.AddressService;
import in.akhilesh.instantcart.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAll(@AuthenticationPrincipal User user) {

        List<AddressResponse> allAddresses = addressService.getAllAddresses(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "All addresses fetched successfully",
                        allAddresses
                ));

    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> create(@AuthenticationPrincipal User user, @Valid @RequestBody AddressRequest req) {

        AddressResponse address = addressService.createAddress(user, req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        "Address created successfully",
                        address
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> update(@AuthenticationPrincipal User user, @PathVariable Long id,
                                  @Valid @RequestBody AddressRequest req) {
        AddressResponse updatedAddress = addressService.updateAddress(user, id, req);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Address updated Successfully",
                        updatedAddress
                ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        addressService.deleteAddress(user,id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.NO_CONTENT.value(),
                        "Address deleted successfully",
                        null
                ));
    }


}
