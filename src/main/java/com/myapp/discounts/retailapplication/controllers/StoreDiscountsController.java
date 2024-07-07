package com.myapp.discounts.retailapplication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.discounts.retailapplication.exception.InvalidUserException;
import com.myapp.discounts.retailapplication.service.StoreDiscountsService;

@RestController
public class StoreDiscountsController {


    private StoreDiscountsService storeDiscountsService;

    @Autowired
    public StoreDiscountsController(StoreDiscountsService storeDiscountsService){
        this.storeDiscountsService = storeDiscountsService;
    }

    @GetMapping(value = "/applyDiscounts")
    public ResponseEntity<String> applyDiscounts(@RequestParam(value = "userType", required = true)
     String userType,
            @RequestParam(value = "userAssociationInYears", required = false, defaultValue = "1") 
            Integer userAssociationInYears,
            @RequestParam(value = "amount", required = true) Double amount,
            @RequestParam(value = "isProductAGrocery", required = true) Boolean isProductAGrocery)
            {
                final double netPayableAmount = storeDiscountsService.applyDiscounts
                (userType, userAssociationInYears,amount,isProductAGrocery);
                return new ResponseEntity<>(String.valueOf(netPayableAmount), HttpStatus.OK);
    }

    @ExceptionHandler(value = InvalidUserException.class)
    public ResponseEntity<String>  handleInvalidUserException(InvalidUserException invalidUserException) {
        return new ResponseEntity<>(invalidUserException.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
