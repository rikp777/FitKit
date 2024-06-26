package nl.rikp.customerService.service;

import nl.rikp.customerService.dto.CustomerRequest;
import nl.rikp.customerService.dto.CustomerResponse;
import nl.rikp.customerService.dto.FoodPreferenceGroupsResponse;
import nl.rikp.customerService.exception.notFound.CustomerNotFoundException;
import nl.rikp.customerService.mapper.CustomerMapper;
import nl.rikp.customerService.model.*;
import nl.rikp.customerService.repository.CustomerRepository;
import nl.rikp.customerService.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    private Customer customer;
    private CustomerRequest customerRequest;
    private CustomerResponse customerResponse;
    private FoodItem foodItem;
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

        customerRequest = CustomerRequest.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
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

        foodAllergy = FoodAllergy.builder()
                        .id(1L)
                        .customer(customer)
                        .foodAllergyId(100L)
                        .build();
    }

    @Test
    public void createCustomer_ShouldReturnCustomerResponse() {
        Mockito.when(customerMapper.toCustomer(customerRequest)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);
        Mockito.when(customerMapper.fromCustomer(customer)).thenReturn(customerResponse);

        CustomerResponse response = customerService.createNewCustomer(customerRequest);

        assertNotNull(response);
        assertEquals(customerResponse, response);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void updateCustomer_ShouldReturnUpdatedCustomerResponse() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.fromCustomer(customer)).thenReturn(customerResponse);

        CustomerResponse response = customerService.updateExistingCustomer(1L, customerRequest);

        assertNotNull(response);
        assertEquals(customerResponse, response);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void findAllCustomers_ShouldReturnPageOfCustomerResponse() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Customer> page = new PageImpl<>(List.of(customer));
        when(customerRepository.findAll(pageable)).thenReturn(page);
        when(customerMapper.fromCustomer(customer)).thenReturn(customerResponse);

        Page<CustomerResponse> responsePage = customerService.getAllCustomers(0, 10);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(customerResponse, responsePage.getContent().get(0));
    }

    @Test
    public void findCustomerById_ShouldReturnCustomerResponse() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.fromCustomer(customer)).thenReturn(customerResponse);

        CustomerResponse response = customerService.getCustomerById(1L);

        assertNotNull(response);
        assertEquals(customerResponse, response);
    }

    @Test
    public void findCustomerById_ShouldThrowCustomerNotFoundException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    public void deleteCustomerById_ShouldInvokeRepositoryDelete() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.removeCustomer(1L);

        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    public void deleteCustomerById_ShouldThrowCustomerNotFoundException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));
    }
}


