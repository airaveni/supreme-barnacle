package com.autoscout.coding.test.controller;

import com.autoscout.coding.test.Service.AutoScoutCSVReader;
import com.autoscout.coding.test.Service.AutoScoutComputeService;
import com.autoscout.coding.test.model.Contact;
import com.autoscout.coding.test.model.Listing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;
import java.util.List;
import java.util.Map;

/**
 *
 * RestController Responsible for exposing all the API endpoints as per requirements
 * @author  Ashok Kumar Iraveni
 *
 */
@RestController
public class AutoScoutController {

    @Autowired
    private AutoScoutCSVReader autoScoutCSVReader;

    @Autowired
    private AutoScoutComputeService autoScoutComputeService;

    @GetMapping("/listing")
    public List<Listing> getAllListing() {
        return autoScoutCSVReader.readListings();
    }

    @GetMapping("/contact")
    public List<Contact> getAllContacts() {
        return autoScoutCSVReader.readContacts();
    }

    @GetMapping("/averageSellingPriceBySellerType")
    public Map<String, Double> getAverage() {
        return autoScoutComputeService.calculateAverageListingSellingPricePerSellerType();
    }

    @GetMapping("/percentageByMake")
    public Map<String,Double> getPercentageByMake(){
        return autoScoutComputeService.calculatePercentageDistributionByMake();
    }

    @GetMapping("/mostContactedListingByMonth/{month}")
    public List<List<String>> mostContactedListingByMonth(@PathVariable Month month){
        return  autoScoutComputeService.mostContactedListingByMonth(month);
    }
    @GetMapping("/averagePriceOf30MostContacted")
    public Double averagePriceOf30PercentMostContacted(){
        return autoScoutComputeService.averagePriceOf30PercentMostContacted();
    }

}
