package com.hms.mds.oms.controller;

import com.hms.mds.oms.controller.errors.ExceptionTranslator;
import com.hms.mds.oms.data.DummyCustomerBuilder;
import com.hms.mds.oms.data.DummyOrderBuilder;
import com.hms.mds.oms.dto.OrderDto;
import com.hms.mds.oms.entity.Customer;
import com.hms.mds.oms.entity.Order;
import com.hms.mds.oms.mapper.OrderMapper;
import com.hms.mds.oms.repository.CustomerRepository;
import com.hms.mds.oms.repository.OrderRepository;
import com.hms.mds.oms.service.OrderService;
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

import static com.hms.mds.oms.asserts.MvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class OrderControllerIT {

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private Validator validator;

    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderMapper orderMapper;

    OrderDto orderDto;
    Customer customer;

    private static final String API_ORDER = "/api/order";
    private static final String API_ORDER_WITH_ID = API_ORDER + "/{id}";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderController resource = new OrderController(orderService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(resource)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator).build();
    }

    @BeforeEach
    public void initData(){
        orderDto = DummyOrderBuilder.getOrderDto();
        customer = DummyCustomerBuilder.getCustomerEntity();
    }

    @Test
    @Transactional
    public void createOrder_Successful() throws Exception {
        customer = customerRepository.save(customer);
        orderDto.setCustomerId(customer.getId());

        mockMvc.perform(post((API_ORDER))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderDto)))
                .andExpect(status().isCreated())
                .andExpect(assertOrderResponseBody(orderDto));
    }

    @Test
    @Transactional
    public void createOrder_Validation_RequiredFields() throws Exception {
        mockMvc.perform(post((API_ORDER))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new OrderDto())))
                .andExpect(status().isBadRequest())
                .andExpect(isValidationErrorResponse())
                .andExpect(hasFieldErrorCount(3))
                .andExpect(hasFieldError("orderNo", ValidationErrors.MSG_NOT_NULL))
                .andExpect(hasFieldError("orderDate", ValidationErrors.MSG_NOT_NULL))
                .andExpect(hasFieldError("status", ValidationErrors.MSG_NOT_BLANK));
    }

    @Test
    @Transactional
    public void createOrder_Fail_WhenCustomerIdIsInvalid() throws Exception {
        orderDto.setCustomerId(Integer.MAX_VALUE);
        mockMvc.perform(post((API_ORDER))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderDto)))
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_CUSTOMER_NOT_FOUND));
    }

    @Test
    @Transactional
    public void updateOrder_Successful() throws Exception {
        Order order = orderRepository.save(orderMapper.fromDto(orderDto));

        orderDto.setOrderNo(10008L);
        orderDto.setStatus("SOLD_OUT");

        mockMvc.perform(put((API_ORDER_WITH_ID), order.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderDto)))
                .andExpect(status().isOk())
                .andExpect(assertOrderResponseBody(orderDto))
                .andExpect(jsonPath("$.id").value(order.getId()));
    }

    @Test
    @Transactional
    public void updateOrder_Validation_RequiredFields() throws Exception {
        mockMvc.perform(put((API_ORDER_WITH_ID), Integer.MAX_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new OrderDto())))
                .andExpect(status().isBadRequest())
                .andExpect(isValidationErrorResponse())
                .andExpect(hasFieldErrorCount(3))
                .andExpect(hasFieldError("orderNo", ValidationErrors.MSG_NOT_NULL))
                .andExpect(hasFieldError("orderDate", ValidationErrors.MSG_NOT_NULL))
                .andExpect(hasFieldError("status", ValidationErrors.MSG_NOT_BLANK));
    }

    @Test
    @Transactional
    public void updateOrder_Fail_WhenOrderIdIsInvalid() throws Exception {
        mockMvc.perform(put((API_ORDER_WITH_ID), Integer.MAX_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderDto)))
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_ORDER_NOT_FOUND));
    }

    @Test
    @Transactional
    public void updateOrder_Fail_WhenCustomerIdIsInvalid() throws Exception {
        Order order = orderRepository.save(orderMapper.fromDto(orderDto));
        orderDto.setCustomerId(Integer.MAX_VALUE);

        mockMvc.perform(put((API_ORDER_WITH_ID), order.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderDto)))
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_CUSTOMER_NOT_FOUND));
    }

    @Test
    @Transactional
    public void deleteOrder_Successful() throws Exception {

        Order order = orderRepository.save(orderMapper.fromDto(orderDto));

        mockMvc.perform(delete((API_ORDER_WITH_ID), order.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    public void deleteOrder_Fail_WhenOrderIdIsInvalid() throws Exception {
        mockMvc.perform(delete((API_ORDER_WITH_ID), Integer.MAX_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_ORDER_NOT_FOUND));
    }

    @Test
    @Transactional
    public void getOrderById_Successful() throws Exception {

        Order order = orderRepository.save(orderMapper.fromDto(orderDto));

        mockMvc.perform(get((API_ORDER_WITH_ID), order.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(assertOrderResponseBody(orderDto))
                .andExpect(jsonPath("$.id").value(order.getId()));
    }

    @Test
    @Transactional
    public void getOrderById_Fail_WhenOrderIdIsInvalid() throws Exception {
        mockMvc.perform(get((API_ORDER_WITH_ID), Integer.MAX_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_ORDER_NOT_FOUND));
    }

    @Test
    @Transactional
    public void getOrders_Successful() throws Exception {
        customer = customerRepository.save(customer);

        List<Order> orders = DummyOrderBuilder.getOrderEntities();
        orders.get(0).setCustomer(customer);

        orderRepository.saveAll(orders);

        mockMvc.perform(get((API_ORDER)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(assertOrders(orders));
    }


    private ResultMatcher assertOrderResponseBody(OrderDto order) {
        return mvcResult -> {
            jsonPath("$.id").value(Matchers.notNullValue()).match(mvcResult);
            jsonPath("$.orderNo").value(order.getOrderNo()).match(mvcResult);
            jsonPath("$.orderDate").value(order.getOrderDate().toString()).match(mvcResult);
            jsonPath("$.status").value(order.getStatus()).match(mvcResult);
            jsonPath("$.customerId").value(order.getCustomerId()).match(mvcResult);
        };
    }

    private ResultMatcher assertOrders(List<Order> expectedOrders) {
        return mvcResult -> {
            jsonPath("$.length()").value(expectedOrders.size()).match(mvcResult);

            for (int i = 0; i < expectedOrders.size(); i++) {
                Order order = expectedOrders.get(i);
                String orderPath = "$[" + i + "]";
                jsonPath(orderPath + ".id").isNumber().match(mvcResult);
                jsonPath(orderPath + ".orderNo").value(order.getOrderNo()).match(mvcResult);
                jsonPath(orderPath + ".orderDate").value(order.getOrderDate().toString()).match(mvcResult);
                jsonPath(orderPath + ".status").value(order.getStatus()).match(mvcResult);
                jsonPath(orderPath + ".customerId").value(order.getCustomer() != null? order.getCustomer().getId(): null).match(mvcResult);
            }
        };
    }
}
