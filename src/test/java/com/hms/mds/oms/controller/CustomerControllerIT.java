package com.hms.mds.oms.controller;


import com.hms.mds.oms.controller.errors.ExceptionTranslator;
import com.hms.mds.oms.data.DummyCustomerBuilder;
import com.hms.mds.oms.dto.CustomerDto;
import com.hms.mds.oms.entity.Customer;
import com.hms.mds.oms.mapper.CustomerMapper;
import com.hms.mds.oms.repository.CustomerRepository;
import com.hms.mds.oms.service.CustomerService;
import com.hms.mds.oms.util.Constants;
import com.hms.mds.oms.util.ValidationErrors;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.hms.mds.oms.asserts.MvcResultMatchers.*;

@SpringBootTest
public class CustomerControllerIT {

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private Validator validator;

    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    CustomerDto customerDto;

    private static final String API_CUSTOMER = "/api/customer";
    private static final String API_CUSTOMER_WITH_ID = API_CUSTOMER + "/{id}";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerController resource = new CustomerController(customerService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(resource)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator).build();
    }

    @BeforeEach
    public void initData(){
        customerDto = DummyCustomerBuilder.getCustomerDto();
    }

    @Test
    @Transactional
    public void createCustomer_Successful() throws Exception {
        mockMvc.perform(post((API_CUSTOMER))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDto)))
                .andExpect(status().isCreated())
                .andExpect(assertCustomerResponseBody(customerDto));
    }

    @Test
    @Transactional
    public void createCustomer_Validation_RequiredFields() throws Exception {
        mockMvc.perform(post((API_CUSTOMER))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new CustomerDto())))
                .andExpect(status().isBadRequest())
                .andExpect(isValidationErrorResponse())
                .andExpect(hasFieldErrorCount(4))
                .andExpect(hasFieldError("name", ValidationErrors.MSG_NOT_BLANK))
                .andExpect(hasFieldError("email", ValidationErrors.MSG_NOT_BLANK))
                .andExpect(hasFieldError("phone", ValidationErrors.MSG_NOT_BLANK))
                .andExpect(hasFieldError("address", ValidationErrors.MSG_NOT_BLANK));;
    }

    @Test
    @Transactional
    public void updateCustomer_Successful() throws Exception {
        Customer customer = customerRepository.save(customerMapper.fromDto(customerDto));

        customerDto.setName(customerDto.getName() + " - Updated");
        customerDto.setAddress(customerDto.getAddress() + " - Updated");

        mockMvc.perform(put((API_CUSTOMER_WITH_ID), customer.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDto)))
                .andExpect(status().isOk())
                .andExpect(assertCustomerResponseBody(customerDto))
                .andExpect(jsonPath("$.id").value(customer.getId()));
    }

    @Test
    @Transactional
    public void updateCustomer_Validation_RequiredFields() throws Exception {
        mockMvc.perform(put((API_CUSTOMER_WITH_ID), Integer.MAX_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new CustomerDto())))
                .andExpect(status().isBadRequest())
                .andExpect(isValidationErrorResponse())
                .andExpect(hasFieldErrorCount(4))
                .andExpect(hasFieldError("name", ValidationErrors.MSG_NOT_BLANK))
                .andExpect(hasFieldError("email", ValidationErrors.MSG_NOT_BLANK))
                .andExpect(hasFieldError("phone", ValidationErrors.MSG_NOT_BLANK))
                .andExpect(hasFieldError("address", ValidationErrors.MSG_NOT_BLANK));
    }

    @Test
    @Transactional
    public void updateCustomer_Fail_WhenCustomerIdIsInvalid() throws Exception {
        mockMvc.perform(put((API_CUSTOMER_WITH_ID), Integer.MAX_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDto)))
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_CUSTOMER_NOT_FOUND));
    }


    @Test
    @Transactional
    public void deleteCustomer_Successful() throws Exception {

        Customer customer = customerRepository.save(customerMapper.fromDto(customerDto));

        mockMvc.perform(delete((API_CUSTOMER_WITH_ID), customer.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    public void deleteCustomer_Fail_WhenCustomerIdIsInvalid() throws Exception {
        mockMvc.perform(delete((API_CUSTOMER_WITH_ID), Integer.MAX_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_CUSTOMER_NOT_FOUND));
    }

    @Test
    @Transactional
    public void getCustomerById_Successful() throws Exception {

        Customer customer = customerRepository.save(customerMapper.fromDto(customerDto));

        mockMvc.perform(get((API_CUSTOMER_WITH_ID), customer.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(assertCustomerResponseBody(customerDto))
                .andExpect(jsonPath("$.id").value(customer.getId()));
    }

    @Test
    @Transactional
    public void getCustomerById_Fail_WhenCustomerIdIsInvalid() throws Exception {
        mockMvc.perform(get((API_CUSTOMER_WITH_ID), Integer.MAX_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_CUSTOMER_NOT_FOUND));
    }

    @Test
    @Transactional
    public void getCustomers_Successful() throws Exception {

        List<Customer> customers = DummyCustomerBuilder.getCustomerEntities();
        customerRepository.saveAll(customers);

        mockMvc.perform(get((API_CUSTOMER)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(assertCustomers(customers));
    }


    private ResultMatcher assertCustomerResponseBody(CustomerDto customer) {
        return mvcResult -> {
            jsonPath("$.id").value(Matchers.notNullValue()).match(mvcResult);
            jsonPath("$.name").value(customer.getName()).match(mvcResult);
            jsonPath("$.email").value(customer.getEmail()).match(mvcResult);
            jsonPath("$.phone").value(customer.getPhone()).match(mvcResult);
            jsonPath("$.address").value(customer.getAddress()).match(mvcResult);
        };
    }

    private ResultMatcher assertCustomers(List<Customer> expectedCustomers) {
        return mvcResult -> {
            jsonPath("$.length()").value(expectedCustomers.size()).match(mvcResult);

            for (int i = 0; i < expectedCustomers.size(); i++) {
                Customer customer = expectedCustomers.get(i);
                String customerPath = "$[" + i + "]";
                jsonPath(customerPath + ".id").isNumber().match(mvcResult);
                jsonPath(customerPath + ".name").value(customer.getName()).match(mvcResult);
                jsonPath(customerPath + ".email").value(customer.getEmail()).match(mvcResult);
                jsonPath(customerPath + ".phone").value(customer.getPhone()).match(mvcResult);
                jsonPath(customerPath + ".address").value(customer.getAddress()).match(mvcResult);
            }
        };
    }
}
