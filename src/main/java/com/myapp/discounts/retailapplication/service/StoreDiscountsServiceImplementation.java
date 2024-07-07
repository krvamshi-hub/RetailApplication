package com.myapp.discounts.retailapplication.service;

import org.springframework.stereotype.Service;

import com.myapp.discounts.retailapplication.exception.InvalidUserException;

@Service
public class StoreDiscountsServiceImplementation implements StoreDiscountsService {

    private static final String USER_TYPE_EMPLOYEE = "EMPLOYEE";
    private static final String USER_TYPE_AFFILIATE = "AFFILIATE";
    private static final String USER_TYPE_CUSTOMER = "CUSTOMER";

    private static final Double EMPLOYEE_DISCOUNT = 0.3;
    private static final Double AFFILIATE_DISCOUNT = 0.1;
    private static final Double CUSTOMER_DISCOUNT = 0.05;
    private static final Integer CUSTOMER_ASSOCIATION_ELIGIBILITY_IN_YEARS = 2;
    private static final Integer DISCOUNT_FOR_EVERY_100_BILL = 5;
    private static final String INVALID_USER_DO_NOT_APPLY_DISCOUNT =
     "Invalid User. Discounts cannot be applied";

    @Override
    public Double applyDiscounts(String userType, Integer userAssociationInYears,
            Double originalBillAmount, Boolean isProductAGrocery)
            throws InvalidUserException {

        final Double discountValueForEverySpecificAmountSpent =
         calculateDiscountForEverySpecificAmountSpent(
                originalBillAmount);

        return Boolean.FALSE.equals(isProductAGrocery)
                ? getNetPayableWithDiscountCalculatedByPercentage(userType, userAssociationInYears, 
                originalBillAmount) - discountValueForEverySpecificAmountSpent
                : (originalBillAmount - discountValueForEverySpecificAmountSpent);

    }

    private static Double getNetPayableWithDiscountCalculatedByPercentage(String userType,
            Integer userAssociationInYears, Double originalBillAmount) throws InvalidUserException {
        return switch (userType.toUpperCase()) {
            case USER_TYPE_EMPLOYEE -> originalBillAmount - (originalBillAmount * EMPLOYEE_DISCOUNT);
            case USER_TYPE_AFFILIATE -> originalBillAmount - (originalBillAmount * AFFILIATE_DISCOUNT);
            case USER_TYPE_CUSTOMER ->
                applyDiscountBasedOnCustomerAssociation(originalBillAmount, userAssociationInYears);
            default -> throw new InvalidUserException(INVALID_USER_DO_NOT_APPLY_DISCOUNT);
        };
    }

    private static Double applyDiscountBasedOnCustomerAssociation(Double originalBillAmount,
            Integer userAssociationInYears) {
        if (userAssociationInYears >= CUSTOMER_ASSOCIATION_ELIGIBILITY_IN_YEARS) {
            return originalBillAmount - (originalBillAmount * CUSTOMER_DISCOUNT);
        } else {
            return originalBillAmount * 1d;
        }
    }

    private Double calculateDiscountForEverySpecificAmountSpent(Double originalBillAmount) {
        if (originalBillAmount < 100) {
            return 0d;
        } else {
            return (originalBillAmount / 100) * DISCOUNT_FOR_EVERY_100_BILL;
        }
    }
}
