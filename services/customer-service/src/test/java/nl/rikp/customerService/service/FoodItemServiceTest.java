package nl.rikp.customerService.service;

import nl.rikp.customerService.dto.CustomerResponse;
import nl.rikp.customerService.dto.FoodPreferenceGroupsResponse;
import nl.rikp.customerService.exception.PremiumFeatureException;
import nl.rikp.customerService.exception.notFound.CustomerNotFoundException;
import nl.rikp.customerService.mapper.CustomerMapper;
import nl.rikp.customerService.model.*;
import nl.rikp.customerService.repository.CustomerRepository;
import nl.rikp.customerService.service.impl.FoodItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FoodItemServiceTest {
    @InjectMocks
    private FoodItemServiceImpl foodItemService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    private Customer customer;
    private CustomerResponse customerResponse;
    private FoodItem foodItem;

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

        foodItem = FoodItem.builder()
                .id(1L)
                .customer(customer)
                .foodItemId(100L)
                .preferenceType(FoodItemPreferenceType.PREFERRED)
                .foodItemPriorityLevel(FoodItemPriorityLevel.FIVE)
                .build();
    }

    @Test
    public void addFoodItem_ShouldReturnUpdatedCustomerResponse() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.fromCustomer(customer)).thenReturn(customerResponse);
        when(customerRepository.findByFoodItemId(1L, 100L)).thenReturn(Optional.of(foodItem));

        CustomerResponse response = foodItemService.addFoodItem(1L, 100L, FoodItemPreferenceType.PREFERRED, FoodItemPriorityLevel.FIVE);

        assertNotNull(response);
        assertEquals(customerResponse, response);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void addFoodItem_ShouldThrowCustomerNotFoundException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(CustomerNotFoundException.class, () -> foodItemService.addFoodItem(1L, 100L, FoodItemPreferenceType.PREFERRED, FoodItemPriorityLevel.FIVE));
    }

    @Test
    public void addFoodItem_ShouldThrowIllegalStateException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByFoodItemIdAndPreferenceType(1L, 1L, FoodItemPreferenceType.PREFERRED)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> foodItemService.addFoodItem(1L, 1L, FoodItemPreferenceType.PREFERRED, FoodItemPriorityLevel.FIVE));
    }

    @Test
    public void addFoodItem_ShouldThrowPremiumFeatureException_BASIC() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByFoodItemIdAndPreferenceType(1L, 1L, FoodItemPreferenceType.PREFERRED)).thenReturn(false);
        when(customerRepository.findAllByFoodPreferenceType(1L, FoodItemPreferenceType.PREFERRED)).thenReturn(List.of(foodItem, foodItem, foodItem, foodItem, foodItem));

        assertThrows(PremiumFeatureException.class, () -> foodItemService.addFoodItem(1L, 1L, FoodItemPreferenceType.PREFERRED, FoodItemPriorityLevel.FIVE));
    }

    @Test
    public void addFoodItem_ShouldThrowPremiumFeatureException_NONE() {
        customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.nl")
                .phone("1234567890")
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByFoodItemIdAndPreferenceType(1L, 1L, FoodItemPreferenceType.PREFERRED)).thenReturn(false);
        when(customerRepository.findAllByFoodPreferenceType(1L, FoodItemPreferenceType.PREFERRED)).thenReturn(List.of(foodItem, foodItem));

        assertThrows(PremiumFeatureException.class, () -> foodItemService.addFoodItem(1L, 1L, FoodItemPreferenceType.PREFERRED, FoodItemPriorityLevel.FIVE));
    }

    @Test
    public void updateFoodItem_ShouldReturnUpdatedCustomerResponse() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.findByFoodItemId(1L, 100L)).thenReturn(Optional.of(foodItem));
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.fromCustomer(customer)).thenReturn(customerResponse);

        CustomerResponse response = foodItemService.updateFoodItem(1L, 100L, FoodItemPreferenceType.PREFERRED, FoodItemPriorityLevel.FIVE);

        assertNotNull(response);
        assertEquals(customerResponse, response);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void removeFoodItem_ShouldInvokeRepositorySave() {
        customer.addFoodItem(foodItem);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.findByFoodItemId(1L, 100L)).thenReturn(Optional.of(foodItem));

        foodItemService.removeFoodItem(1L, 100L);

        verify(customerRepository, times(1)).save(customer);
    }
}



