package com.autoscout.coding.test.Service;

import com.autoscout.coding.test.model.Contact;
import com.autoscout.coding.test.model.Listing;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AutoScoutComputeServiceTest {

    @InjectMocks
    AutoScoutComputeService autoScoutComputeService;

    @Mock
    AutoScoutCSVReader autoScoutCSVReader;

    @Test
    public void test_calculate_average_listing_selling_price_per_SellerType() {

        //given
        when(autoScoutCSVReader.readListings()).thenReturn(createListings());

        //when
        Map<String, Double> returnData = autoScoutComputeService.calculateAverageListingSellingPricePerSellerType();

        //then assert below
        // avg of 1500 + 1500
        assertEquals(1500, (double) returnData.get("private"), 0.0);
        // avg of 18ß0 + 2000
        assertEquals(1900, (double) returnData.get("other"), 0.0);
        //avg of 1200 + 2000 + 1000
        assertEquals(1400, (double) returnData.get("dealer"), 0.0);

    }


    @Test
    public void test_calculate_percentage_distribution_dy_make() {
        //given
        when(autoScoutCSVReader.readListings()).thenReturn(createListings());

        //when
        Map<String, Double> returnData = autoScoutComputeService.calculatePercentageDistributionByMake();

        //then assert below
        // 3 out of total 7 i.e 42%
        assertEquals(42, returnData.get("BMW"), 0.0);
        // 2 out of total 7 i.e 28%
        assertEquals(28, returnData.get("AUDI"), 0.0);
        // 2 out of total 7 i.e 28%
        assertEquals(28, returnData.get("VW"), 0.0);


    }

    @Test
    public void test_most_contacted_listing_by_month() {

        //given
        when(autoScoutCSVReader.readContacts()).thenReturn(createContacts());
        when(autoScoutCSVReader.readListings()).thenReturn(createListings());

        //when
        List<List<String>> mostContactedList = autoScoutComputeService.mostContactedListingByMonth(Month.JANUARY);

        //then
        // Listing one(1) has been contacted thrice in January
        assertEquals(mostContactedList.get(0).get(4), "3");
        // Listing one(2) has been contacted twice in January
        assertEquals(mostContactedList.get(1).get(4), "2");


    }

    @Test
    public void test_average_price_of_30Percent_most_contacted() {

        //given
        when(autoScoutCSVReader.readContacts()).thenReturn(createContacts());
        when(autoScoutCSVReader.readListings()).thenReturn(createListings());

        //when
       double average = autoScoutComputeService.averagePriceOf30PercentMostContacted();

       //top 30% will be listing id with 1 and average is same as its price
        assertEquals(1200, average, 0.0);


    }


    private List<Listing> createListings() {
        List<Listing> listingList = new ArrayList<>();
        Listing listing1 = new Listing(1, "BMW", 1200, 1500, "dealer");
        Listing listing2 = new Listing(2, "AUDI", 1800, 1500, "other");
        Listing listing3 = new Listing(3, "VW", 2000, 1500, "dealer");

        Listing listing4 = new Listing(4, "AUDI", 1800, 1500, "private");
        Listing listing5 = new Listing(5, "BMW", 1200, 1500, "private");
        Listing listing6 = new Listing(6, "VW", 2000, 1500, "other");

        Listing listing7 = new Listing(7, "BMW", 1000, 1500, "dealer");

        listingList.add(listing1);
        listingList.add(listing2);
        listingList.add(listing3);
        listingList.add(listing4);
        listingList.add(listing5);
        listingList.add(listing6);
        listingList.add(listing7);

        return listingList;
    }

    public List<Contact> createContacts() {
        List<Contact> contactList = new ArrayList<>();

        Contact contact1 = new Contact(1, LocalDateTime.of(2020, Month.JANUARY, 1, 19, 20, 1));
        Contact contact2 = new Contact(1, LocalDateTime.of(2020, Month.JANUARY, 3, 19, 20, 1));
        Contact contact3 = new Contact(2, LocalDateTime.of(2020, Month.JANUARY, 10, 19, 20, 1));
        Contact contact4 = new Contact(2, LocalDateTime.of(2020, Month.FEBRUARY, 13, 19, 20, 1));

        Contact contact5 = new Contact(1, LocalDateTime.of(2020, Month.JANUARY, 1, 19, 20, 1));
        Contact contact6 = new Contact(1, LocalDateTime.of(2020, Month.FEBRUARY, 3, 19, 20, 1));
        Contact contact7 = new Contact(2, LocalDateTime.of(2020, Month.FEBRUARY, 4, 19, 20, 1));
        Contact contact8 = new Contact(2, LocalDateTime.of(2020, Month.JANUARY, 5, 19, 20, 1));

        contactList.add(contact1);
        contactList.add(contact2);
        contactList.add(contact3);
        contactList.add(contact4);
        contactList.add(contact5);
        contactList.add(contact6);
        contactList.add(contact7);
        contactList.add(contact8);
        return contactList;
    }


}