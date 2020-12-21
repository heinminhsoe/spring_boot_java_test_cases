package com.hms.mds.oms.asserts;

import com.hms.mds.oms.util.ValidationErrors;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public final class MvcResultMatchers {

    private MvcResultMatchers() {
    }

    public static ResultMatcher isValidationErrorResponse() {
        return isErrorResponse(ValidationErrors.INVALID_ARGUMENT);
    }

    public static ResultMatcher hasFieldErrorCount(int errorCount) {
        return mvcResult -> jsonPath("$.fieldErrors.length()").value(errorCount).match(mvcResult);
    }

    public static ResultMatcher hasFieldError(String fieldName, String... errors) {
        return mvcResult -> jsonPath("$.fieldErrors[?(@.field=='" + fieldName + "')].message", containsInAnyOrder(errors)).match(mvcResult);
    }

    public static ResultMatcher isErrorResponse(String message) {
        return mvcResult -> {
            jsonPath("$.message").value(message).match(mvcResult);
        };
    }
}
