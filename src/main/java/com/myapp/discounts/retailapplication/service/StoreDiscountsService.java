package com.myapp.discounts.retailapplication.service;

import com.myapp.discounts.retailapplication.exception.InvalidUserException;

public interface StoreDiscountsService {

    Double applyDiscounts
    (String userType, Integer userAssociationInYears, Double amount, Boolean isProductAGrocery)
    throws InvalidUserException;

}
