package com.autoscout.coding.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Listing {

    private long id;

    private String make;

    private long price;

    private long mileage;

    private String sellerType;

}
