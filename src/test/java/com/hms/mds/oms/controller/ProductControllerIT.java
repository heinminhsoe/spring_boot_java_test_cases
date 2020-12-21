package com.hms.mds.oms.controller;

import com.hms.mds.oms.controller.errors.ExceptionTranslator;
import com.hms.mds.oms.data.DummyProductBuilder;
import com.hms.mds.oms.dto.ProductDto;
import com.hms.mds.oms.entity.Product;
import com.hms.mds.oms.mapper.ProductMapper;
import com.hms.mds.oms.repository.ProductRepository;
import com.hms.mds.oms.service.ProductService;
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
public class ProductControllerIT {

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private Validator validator;

    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    ProductDto productDto;

    private static final String API_PRODUCT = "/api/product";
    private static final String API_PRODUCT_WITH_ID = API_PRODUCT + "/{id}";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductController resource = new ProductController(productService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(resource)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator).build();
    }

    @BeforeEach
    public void initData(){
        productDto = DummyProductBuilder.getProductDto();
    }

    @Test
    @Transactional
    public void createProduct_Successful() throws Exception {
        mockMvc.perform(post((API_PRODUCT))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productDto)))
                .andExpect(status().isCreated())
                .andExpect(assertProductResponseBody(productDto));
    }

    @Test
    @Transactional
    public void createProduct_Validation_RequiredFields() throws Exception {
        mockMvc.perform(post((API_PRODUCT))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new ProductDto())))
                .andExpect(status().isBadRequest())
                .andExpect(isValidationErrorResponse())
                .andExpect(hasFieldErrorCount(2))
                .andExpect(hasFieldError("name", ValidationErrors.MSG_NOT_BLANK))
                .andExpect(hasFieldError("unitPrice", ValidationErrors.MSG_NOT_NULL));
    }

    @Test
    @Transactional
    public void updateProduct_Successful() throws Exception {
        Product product = productRepository.save(productMapper.fromDto(productDto));

        productDto.setName(productDto.getName() + "- Updated");
        productDto.setUnitPrice(50000D);

        mockMvc.perform(put((API_PRODUCT_WITH_ID), product.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productDto)))
                .andExpect(status().isOk())
                .andExpect(assertProductResponseBody(productDto))
                .andExpect(jsonPath("$.id").value(product.getId()));
    }

    @Test
    @Transactional
    public void updateProduct_Validation_RequiredFields() throws Exception {
        mockMvc.perform(put((API_PRODUCT_WITH_ID), Integer.MAX_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new ProductDto())))
                .andExpect(status().isBadRequest())
                .andExpect(isValidationErrorResponse())
                .andExpect(hasFieldErrorCount(2))
                .andExpect(hasFieldError("name", ValidationErrors.MSG_NOT_BLANK))
                .andExpect(hasFieldError("unitPrice", ValidationErrors.MSG_NOT_NULL));
    }

    @Test
    @Transactional
    public void updateProduct_Fail_WhenProductIdIsInvalid() throws Exception {
        mockMvc.perform(put((API_PRODUCT_WITH_ID), Integer.MAX_VALUE)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productDto)))
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_PRODUCT_NOT_FOUND));
    }


    @Test
    @Transactional
    public void deleteProduct_Successful() throws Exception {

        Product product = productRepository.save(productMapper.fromDto(productDto));

        mockMvc.perform(delete((API_PRODUCT_WITH_ID), product.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    public void deleteProduct_Fail_WhenProductIdIsInvalid() throws Exception {
        mockMvc.perform(delete((API_PRODUCT_WITH_ID), Integer.MAX_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_PRODUCT_NOT_FOUND));
    }

    @Test
    @Transactional
    public void getProductById_Successful() throws Exception {

        Product product = productRepository.save(productMapper.fromDto(productDto));

        mockMvc.perform(get((API_PRODUCT_WITH_ID), product.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(assertProductResponseBody(productDto))
                .andExpect(jsonPath("$.id").value(product.getId()));
    }

    @Test
    @Transactional
    public void getProductById_Fail_WhenProductIdIsInvalid() throws Exception {
        mockMvc.perform(get((API_PRODUCT_WITH_ID), Integer.MAX_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(isErrorResponse(Constants.MSG_PRODUCT_NOT_FOUND));
    }

    @Test
    @Transactional
    public void getProducts_Successful() throws Exception {

        List<Product> products = DummyProductBuilder.getProductEntities();
        productRepository.saveAll(products);

        mockMvc.perform(get((API_PRODUCT)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(assertProducts(products));
    }


    private ResultMatcher assertProductResponseBody(ProductDto product) {
        return mvcResult -> {
            jsonPath("$.id").value(Matchers.notNullValue()).match(mvcResult);
            jsonPath("$.name").value(product.getName()).match(mvcResult);
            jsonPath("$.unitPrice").value(product.getUnitPrice()).match(mvcResult);
        };
    }

    private ResultMatcher assertProducts(List<Product> expectedProducts) {
        return mvcResult -> {
            jsonPath("$.length()").value(expectedProducts.size()).match(mvcResult);

            for (int i = 0; i < expectedProducts.size(); i++) {
                Product product = expectedProducts.get(i);
                String productPath = "$[" + i + "]";
                jsonPath(productPath + ".id").isNumber().match(mvcResult);
                jsonPath(productPath + ".name").value(product.getName()).match(mvcResult);
                jsonPath(productPath + ".unitPrice").value(product.getUnitPrice()).match(mvcResult);
            }
        };
    }
}
