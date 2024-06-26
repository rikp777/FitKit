package nl.rikp.customerService.service;

import nl.rikp.customerService.dto.CustomerResponse;
import nl.rikp.customerService.dto.FoodPreferenceGroupsResponse;
import nl.rikp.customerService.exception.notFound.CustomerNotFoundException;
import nl.rikp.customerService.mapper.CustomerMapper;
import nl.rikp.customerService.model.Customer;
import nl.rikp.customerService.model.FoodAllergy;
import nl.rikp.customerService.model.PremiumLevel;
import nl.rikp.customerService.repository.CustomerRepository;
import nl.rikp.customerService.service.impl.FoodAllergyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FoodAllergyServiceTest {
    @InjectMocks
    private FoodAllergyServiceImpl foodAllergyService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    private Customer customer;
    private CustomerResponse customerResponse;
    private FoodAllergy foodAllergy;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.nl")
                .phone("1234567890")
                .premiumLevel(PremiumLevel.BASIC)
                .build();

        customerResponse = CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .foodPreferenceGroups(
                        FoodPreferenceGroupsResponse.builder()
                                .disliked(new ArrayList<>())
                                .neutrals(new ArrayList<>())
                                .preferred(new ArrayList<>())
                                .build())
                .build();

        foodAllergy = FoodAllergy.builder()
                .id(1L)
                .customer(customer)
                .foodAllergyId(100L)
                .build();
    }

    @Test
    public void addFoodAllergy_ShouldReturnUpdatedCustomerResponse() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.fromCustomer(customer)).thenReturn(customerResponse);
        when(customerRepository.findByFoodAllergyId(1L, 100L)).thenReturn(Optional.empty());

        CustomerResponse response = foodAllergyService.addFoodAllergy(1L, 100L);

        assertNotNull(response);
        assertEquals(customerResponse, response);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void addFoodAllergy_ShouldThrowCustomerNotFoundException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(CustomerNotFoundException.class, () -> foodAllergyService.addFoodAllergy(1L, 100L));
    }

    @Test
    public void addFoodAllergy_ShouldThrowIllegalStateException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.findByFoodAllergyId(1L, 1L)).thenReturn(Optional.of(foodAllergy));

        assertThrows(IllegalStateException.class, () -> foodAllergyService.addFoodAllergy(1L, 1L));
    }
}




