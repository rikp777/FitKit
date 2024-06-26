package nl.rikp.customerService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.rikp.customerService.dto.CustomerRequest;
import nl.rikp.customerService.dto.CustomerResponse;
import nl.rikp.customerService.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@Validated
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> findAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(customerService.getAllCustomers(page, size));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsByIdCustomer(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(customerService.doesCustomerExist(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findByIdCustomer(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @RequestBody @Valid CustomerRequest request
    ){
        var newCustomer = customerService.createNewCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newCustomer);
    }

    @PutMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable("customer-id") Long customerId,
            @RequestBody @Valid CustomerRequest request
    ){
        var updatedData = customerService.updateExistingCustomer(customerId, request);
        return ResponseEntity.ok(updatedData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByIdCustomer(
            @PathVariable Long id
    ){
        customerService.removeCustomer(id);
        return ResponseEntity.ok().build();
    }
}
