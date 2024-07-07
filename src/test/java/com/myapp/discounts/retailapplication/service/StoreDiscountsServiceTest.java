package com.myapp.discounts.retailapplication.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.myapp.discounts.retailapplication.exception.InvalidUserException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
class StoreDiscountsServiceTest {

    private static final String USER_TYPE_EMPLOYEE = "EMPLOYEE";
    private static final String USER_TYPE_AFFILIATE = "AFFILIATE";
    private static final String USER_TYPE_CUSTOMER = "CUSTOMER";

    @InjectMocks
    private StoreDiscountsServiceImplementation storeDiscountsServiceImpl;

    @ParameterizedTest
    (name = "{index} - Given an {0} bought grocery:{3} and has been " +
            "associated with store for years - {1}, " +
            "The expected netPayableAmount is {4} ")
    @MethodSource("retailStoreDiscountCalculation")
    void givenAUserthenApplyDiscounts(String userType, Integer userAssociationInYears,
    Double originalBillAmount, Boolean isProductAGrocery, Double expectedNetPayableAmount) {
        Double actualNetPayableAmount = storeDiscountsServiceImpl.applyDiscounts
        (userType, userAssociationInYears, originalBillAmount, isProductAGrocery);
        assertEquals(expectedNetPayableAmount, actualNetPayableAmount);
    }

    @Test
    void given_InvalidUser_DiscountsShouldNotApply() {
        Integer userAssociationInYears = null;
        Double originalBillAmount = 200d;
        Boolean isProductAGrocery = false;
        assertThrows(InvalidUserException.class, () -> {
            storeDiscountsServiceImpl.applyDiscounts
            ("XYZ", userAssociationInYears, originalBillAmount,isProductAGrocery );
        });
    }

    private static Stream<Arguments> retailStoreDiscountCalculation() {
        return Stream.of(
       //Example: Arguments(userType, userAssociationInYears, originalBillAmount, isProductAGrocery,expectedNetPayableAmount) 
            Arguments.of(USER_TYPE_EMPLOYEE, null, 300.0, false, 195.0),
            Arguments.of(USER_TYPE_EMPLOYEE, null, 300.0, true, 285.0),
            Arguments.of(USER_TYPE_EMPLOYEE, null, 90.0, false, 63.0),
            Arguments.of(USER_TYPE_EMPLOYEE, null, 90.0, true, 90.0),
            Arguments.of(USER_TYPE_AFFILIATE, null, 300.0, false, 255.0),  
            Arguments.of(USER_TYPE_AFFILIATE, null, 300.0, true, 285.0),
            Arguments.of(USER_TYPE_AFFILIATE, null, 90.0, false, 81.0),
            Arguments.of(USER_TYPE_AFFILIATE, null, 90.0, true, 90.0),
            Arguments.of(USER_TYPE_CUSTOMER, 2, 300.0, false, 270.0),
            Arguments.of(USER_TYPE_CUSTOMER, 2, 300.0, true, 285.0),
            Arguments.of(USER_TYPE_CUSTOMER, 2, 90.0, false, 85.5),
            Arguments.of(USER_TYPE_CUSTOMER, 2, 90.0, true, 90.0),
            Arguments.of(USER_TYPE_CUSTOMER, 1, 300.0, false, 285.0),
            Arguments.of(USER_TYPE_CUSTOMER, 1, 300.0, true, 285.0) 
        );
    }


}
