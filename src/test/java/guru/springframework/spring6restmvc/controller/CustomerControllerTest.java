package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.controller.model.CustomerDTO;
import guru.springframework.spring6restmvc.exception.NotFoundException;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.springframework.spring6restmvc.controller.CustomerController.API_V_1_CUSTOMER;
import static guru.springframework.spring6restmvc.controller.CustomerController.CUSTOMER_PATH_ID;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDTO>customerArgumentCaptor;

    private CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void testDeleteCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);

        doNothing().when(customerService).deleteById(customer.getId());

        mockMvc.perform(delete(CUSTOMER_PATH_ID, customer.getId()))
            .andExpect(status().isAccepted());

        verify(customerService, times(1)).deleteById(uuidArgumentCaptor.capture());

        Assertions.assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    }

    @Test
    void testPatchCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);
        customer.setCustomerName("Nimeni");

        mockMvc.perform(patch(CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
            .andExpect(status().isAccepted());
        verify(customerService, times(1)).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
        Assertions.assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        Assertions.assertThat(customer).isEqualTo(customerArgumentCaptor.getValue());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);
        customer.setCustomerName("Altul");

        given(customerService.updateCustomer(customer.getId(), customer)).willReturn(customer);

        mockMvc.perform(put(CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerName", is("Altul")));

        verify(customerService, times(1)).updateCustomer(any(UUID.class), any(CustomerDTO.class));
    }

    @Test
    void testCreateNewCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);
        customer.setVersion(null);
        customer.setId(null);

        given(customerService.saveCustomer(any(CustomerDTO.class))).willReturn(customerServiceImpl.listCustomers().get(1));

        mockMvc.perform(post(API_V_1_CUSTOMER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }

    @Test
    void getCustomers() throws Exception {

        List<CustomerDTO> customers = customerServiceImpl.listCustomers();

        given(customerService.listCustomers()).willReturn(customers);

        mockMvc.perform(get(API_V_1_CUSTOMER)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[1].customerName", is(customers.get(1).getCustomerName())));

    }

    @Test
    void getCustomerById() throws Exception {

        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);
        given(customerService.getCustomer(customer.getId())).willReturn(Optional.of(customer));

        mockMvc.perform(get(CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())));

    }

    @Test
    void getBeerByIdNotFound() throws Exception {

        given(customerService.getCustomer(any(UUID.class))).willThrow(NotFoundException.class);

        mockMvc.perform(get(CUSTOMER_PATH_ID, UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }
}
