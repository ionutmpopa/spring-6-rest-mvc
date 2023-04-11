package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

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

    private CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().get(0);

        doNothing().when(customerService).deleteById(customer.getCustomerId());

        mockMvc.perform(delete("/api/v1/customer/" + customer.getCustomerId()))
            .andExpect(status().isAccepted());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService, times(1)).deleteById(uuidArgumentCaptor.capture());

        Assertions.assertThat(customer.getCustomerId()).isEqualTo(uuidArgumentCaptor.getValue());

    }

    @Test
    void testPatchCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().get(0);
        customer.setCustomerName("Nimeni");

        mockMvc.perform(patch("/api/v1/customer/" + customer.getCustomerId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
            .andExpect(status().isAccepted());
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService, times(1)).patchCustomerById(uuidArgumentCaptor.capture(), any(Customer.class));
        Assertions.assertThat(customer.getCustomerId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().get(0);
        customer.setCustomerName("Altul");

        given(customerService.updateCustomer(customer.getCustomerId(), customer)).willReturn(customer);

        mockMvc.perform(put("/api/v1/customer/" + customer.getCustomerId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerName", is("Altul")));

        verify(customerService, times(1)).updateCustomer(any(UUID.class), any(Customer.class));
    }

    @Test
    void testCreateNewCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().get(0);
        customer.setVersion(null);
        customer.setCustomerId(null);

        given(customerService.saveCustomer(any(Customer.class))).willReturn(customerServiceImpl.listCustomers().get(1));

        mockMvc.perform(post("/api/v1/customer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }

    @Test
    void getCustomers() throws Exception {

        List<Customer> customers = customerServiceImpl.listCustomers();

        given(customerService.listCustomers()).willReturn(customers);

        mockMvc.perform(get("/api/v1/customer")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[1].customerName", is(customers.get(1).getCustomerName())));

    }

    @Test
    void getCustomerById() throws Exception {

        Customer customer = customerServiceImpl.listCustomers().get(0);
        given(customerService.getCustomer(customer.getCustomerId())).willReturn(customer);

        mockMvc.perform(get("/api/v1/customer/" + customer.getCustomerId().toString())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())));

    }
}
