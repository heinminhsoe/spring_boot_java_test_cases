package com.hms.mds.oms.controller;

import com.hms.mds.oms.controller.errors.ExceptionTranslator;
import com.hms.mds.oms.data.DummyOrderBuilder;
import com.hms.mds.oms.data.DummyOrderItemsBuilder;
import com.hms.mds.oms.data.DummyProductBuilder;
import com.hms.mds.oms.dto.OrderItemsDto;
import com.hms.mds.oms.entity.Order;
import com.hms.mds.oms.entity.OrderItems;
import com.hms.mds.oms.entity.Product;
import com.hms.mds.oms.mapper.OrderItemsMapper;
import com.hms.mds.oms.repository.OrderItemsRepository;
import com.hms.mds.oms.repository.OrderRepository;
import com.hms.mds.oms.repository.ProductRepository;
import com.hms.mds.oms.service.OrderItemsService;
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
public class OrderItemsControllerIT {

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private Validator validator;

    private MockMvc mockMvc;

    @Autowired
    private OrderItemsService orderItemsService;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    OrderItemsDto orderItemsDto;
    Order order;
    Product product;

    private static final String API_ORDER_ITEMS = "/api/order/{orderId}/orderItems";
    private static final String API_ORDER_ITEMS_WITH_ID = API_ORDER_ITEMS + "/{id}";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderItemsController resource = new OrderItemsController(orderItemsService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(resource)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator).build();
    }

    @BeforeEach
    public void initData(){
        orderItemsDto = DummyOrderItemsBuilder.getOrderItemsDto();
        order = DummyOrderBuilder.getOrderEntity();
        product = DummyProductBuilder.getProductEntity();
    }

    @Test
    @Transactional
    public void createOrderItems_Successful() throws Exception {
        order = orderRepository.save(order);
        product = productRepository.save(product);

        orderItemsDto.setProductId(product.getId());

        mockMvc.perform(post((API_ORDER_ITEMS), order.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItemsDto)))
                .andExpect(status().isCreated())
                .andExpect(assertOrderItemsResponseBody(orderItemsDto));
    }

    @Test
    @Transactional
    public void createOrderItems_Validation_RequiredFields() throws Exception {
        mockMvc.perform(post((API_ORDER_ITEMS), Integer.MAX_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new OrderItemsDto())))
                .andExpect(status().isBadRequest())
                .andExpect(isValidationErrorResponse())
                .andExpect(hasFieldErrorCount(3))
                .andExpect(hasFieldError("quantity", ValidationErrors.MSG_NOT_NULL))
                .andExpect(hasFieldError("total", ValidationErrors.MSG_NOT_NULL))
                .andExpect(hasFieldError("productId", ValidationErrors.MSG_NOT_NULL));
    }

    @Test
    @Transactional
    public void createOrderItems_Fail_WhenOrderIdIsInvalid() throws Exception {
        orderItemsDto.setProductId(Integer.MAX_VALUE);

        mockMvc.perform(post((API_ORDER_ITEMS), Integer.MAX_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItemsDto)))
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_ORDER_NOT_FOUND));
    }

    @Test
    @Transactional
    public void createOrderItems_Fail_WhenProductIdIsInvalid() throws Exception {
        order = orderRepository.save(order);

        orderItemsDto.setProductId(Integer.MAX_VALUE);

        mockMvc.perform(post((API_ORDER_ITEMS), order.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItemsDto)))
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_PRODUCT_NOT_FOUND));
    }

    @Test
    @Transactional
    public void updateOrderItems_Successful() throws Exception {

        order = orderRepository.save(order);
        product = productRepository.save(product);

        OrderItems orderItems = orderItemsMapper.fromDto(orderItemsDto);
        orderItems.setOrder(order);
        orderItems.setProduct(product);

        orderItems = orderItemsRepository.save(orderItems);

        orderItemsDto.setQuantity(4);
        orderItemsDto.setTotal(20000D);
        orderItemsDto.setProductId(product.getId());

        mockMvc.perform(put((API_ORDER_ITEMS_WITH_ID), order.getId(), orderItems.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItemsDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(assertOrderItemsResponseBody(orderItemsDto))
                .andExpect(jsonPath("$.id").value(orderItems.getId()));
    }

    @Test
    @Transactional
    public void updateOrderItems_Validation_RequiredFields() throws Exception {
        mockMvc.perform(put((API_ORDER_ITEMS_WITH_ID), Integer.MAX_VALUE, Integer.MAX_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new OrderItemsDto())))
                .andExpect(status().isBadRequest())
                .andExpect(isValidationErrorResponse())
                .andExpect(hasFieldErrorCount(3))
                .andExpect(hasFieldError("quantity", ValidationErrors.MSG_NOT_NULL))
                .andExpect(hasFieldError("total", ValidationErrors.MSG_NOT_NULL))
                .andExpect(hasFieldError("productId", ValidationErrors.MSG_NOT_NULL));
    }

    @Test
    @Transactional
    public void updateOrderItems_Fail_WhenOrderIdAndOrderItemsIdIsInvalid() throws Exception {

        orderItemsDto.setProductId(Integer.MAX_VALUE);

        mockMvc.perform(put((API_ORDER_ITEMS_WITH_ID), Integer.MAX_VALUE, Integer.MAX_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItemsDto)))
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_ORDER_ITEMS_NOT_FOUND));
    }

    @Test
    @Transactional
    public void updateOrderItems_Fail_WhenProductIdIsInvalid() throws Exception {
        order = orderRepository.save(order);
        product = productRepository.save(product);

        OrderItems orderItems = orderItemsMapper.fromDto(orderItemsDto);
        orderItems.setOrder(order);
        orderItems.setProduct(product);

        orderItems = orderItemsRepository.save(orderItems);

        orderItemsDto.setProductId(Integer.MAX_VALUE);

        mockMvc.perform(put((API_ORDER_ITEMS_WITH_ID), order.getId(), orderItems.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderItemsDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_PRODUCT_NOT_FOUND));
    }

    @Test
    @Transactional
    public void deleteOrderItems_Successful() throws Exception {

        order = orderRepository.save(order);
        product = productRepository.save(product);

        OrderItems orderItems = orderItemsMapper.fromDto(orderItemsDto);
        orderItems.setOrder(order);
        orderItems.setProduct(product);

        orderItems = orderItemsRepository.save(orderItems);

        mockMvc.perform(delete((API_ORDER_ITEMS_WITH_ID), order.getId(), orderItems.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    public void deleteOrderItems_Fail_WhenOrderIdAndOrderItemsIdIsInvalid() throws Exception {
        mockMvc.perform(delete((API_ORDER_ITEMS_WITH_ID), Integer.MAX_VALUE, Integer.MAX_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_ORDER_ITEMS_NOT_FOUND));
    }

    @Test
    @Transactional
    public void getOrderItemsById_Successful() throws Exception {

        order = orderRepository.save(order);
        product = productRepository.save(product);

        OrderItems orderItems = orderItemsMapper.fromDto(orderItemsDto);
        orderItems.setOrder(order);
        orderItems.setProduct(product);

        orderItems = orderItemsRepository.save(orderItems);

        orderItemsDto.setProductId(product.getId());

        mockMvc.perform(get((API_ORDER_ITEMS_WITH_ID), order.getId(), orderItems.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(assertOrderItemsResponseBody(orderItemsDto))
                .andExpect(jsonPath("$.id").value(orderItems.getId()));
    }

    @Test
    @Transactional
    public void getOrderItemsById_Fail_WhenOrderItemsIdIsInvalid() throws Exception {
        mockMvc.perform(get((API_ORDER_ITEMS_WITH_ID), Integer.MAX_VALUE, Integer.MAX_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_ORDER_ITEMS_NOT_FOUND));
    }

    @Test
    @Transactional
    public void getOrderItems_Successful() throws Exception {
        order = orderRepository.save(order);
        product = productRepository.save(product);

        List<OrderItems> orderItems = DummyOrderItemsBuilder.getOrderItemsEntities();

        orderItems.forEach(orderItem -> {
            orderItem.setProduct(product);
            orderItem.setOrder(order);
        });

        orderItemsRepository.saveAll(orderItems);

        mockMvc.perform(get((API_ORDER_ITEMS), order.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(assertOrderItems(orderItems));
    }


    private ResultMatcher assertOrderItemsResponseBody(OrderItemsDto orderItems) {
        return mvcResult -> {
            jsonPath("$.id").value(Matchers.notNullValue()).match(mvcResult);
            jsonPath("$.quantity").value(orderItems.getQuantity()).match(mvcResult);
            jsonPath("$.total").value(orderItems.getTotal()).match(mvcResult);
            jsonPath("$.productId").value(orderItems.getProductId()).match(mvcResult);
        };
    }

    private ResultMatcher assertOrderItems(List<OrderItems> expectedOrderItems) {
        return mvcResult -> {
            jsonPath("$.length()").value(expectedOrderItems.size()).match(mvcResult);

            for (int i = 0; i < expectedOrderItems.size(); i++) {
                OrderItems orderItems = expectedOrderItems.get(i);
                String orderItemsPath = "$[" + i + "]";
                jsonPath(orderItemsPath + ".id").isNumber().match(mvcResult);
                jsonPath(orderItemsPath + ".quantity").value(orderItems.getQuantity()).match(mvcResult);
                jsonPath(orderItemsPath + ".total").value(orderItems.getTotal()).match(mvcResult);
                jsonPath(orderItemsPath + ".productId").value(orderItems.getProduct().getId()).match(mvcResult);
            }
        };
    }
}
