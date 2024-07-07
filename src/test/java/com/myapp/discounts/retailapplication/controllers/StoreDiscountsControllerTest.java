package com.myapp.discounts.retailapplication.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.myapp.discounts.retailapplication.exception.InvalidUserException;
import com.myapp.discounts.retailapplication.service.StoreDiscountsService;

@WebMvcTest(StoreDiscountsController.class)
@ExtendWith(MockitoExtension.class)
class StoreDiscountsControllerTest {
    private static final String USER_TYPE_AFFILIATE = "AFFILIATE";
    private static final String INVALID_USER_DO_NOT_APPLY_DISCOUNT =
    "Invalid User. Discounts cannot be applied";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StoreDiscountsService storeDiscountsService;

    @Test
    void testApplyDiscountsForAffiliate() throws Exception {
        Mockito.when(storeDiscountsService
                .applyDiscounts(USER_TYPE_AFFILIATE, 1, 
                (double) 300, false))
                .thenReturn((double) 255);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("userType", USER_TYPE_AFFILIATE);
        requestParams.add("amount", "300");
        requestParams.add("isProductAGrocery", "false");

        MvcResult result = mockMvc.perform(get("/applyDiscounts")
                .params(requestParams))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("255.0", result.getResponse().getContentAsString());
    }

    @Test
    void given_InvalidUser_DiscountsShouldNotApply() throws Exception {
        Mockito.when(storeDiscountsService
                .applyDiscounts("XYZ", 1, 
                (double) 300, false))
                .thenThrow(new InvalidUserException(INVALID_USER_DO_NOT_APPLY_DISCOUNT));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("userType", "XYZ");
        requestParams.add("amount", "300");
        requestParams.add("isProductAGrocery", "false");

        mockMvc.perform(get("/applyDiscounts")
        .params(requestParams))
        .andExpect(status().isBadRequest())
        .andExpect(result -> 
                assertTrue(result.getResolvedException() instanceof InvalidUserException))
        .andExpect(result -> 
                assertEquals(INVALID_USER_DO_NOT_APPLY_DISCOUNT, 
                result.getResolvedException().getMessage()));

    }
    
}
