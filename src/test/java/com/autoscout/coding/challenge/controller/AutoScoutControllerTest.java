package com.autoscout.coding.challenge.controller;

import com.autoscout.coding.challenge.service.AutoScoutCSVReader;
import com.autoscout.coding.challenge.service.AutoScoutComputeService;
import com.autoscout.coding.challenge.model.Contact;
import com.autoscout.coding.challenge.model.Listing;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@WebMvcTest(AutoScoutController.class)
@RunWith(SpringRunner.class)
public class AutoScoutControllerTest {

    @MockBean
    private AutoScoutComputeService autoScoutComputeService;

    @MockBean
    private AutoScoutCSVReader autoScoutCSVReader;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_get_all_listing() throws Exception {
        //given
        Mockito.when(autoScoutCSVReader.readListings()).thenReturn(createListings());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/listing")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..id").isArray());

    }

    @Test
    public void test_get_all_contacts() throws Exception {
        //given
        Mockito.when(autoScoutCSVReader.readContacts()).thenReturn(createContacts());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/contact")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..listingId").isArray());


    }

    @Test
    public void test_average_selling_price_by_seller_type() throws Exception {

        Map<String,Double> givenMap = new HashMap<>();
        givenMap.put("other",24.00);
        givenMap.put("dealer",25.00);
        givenMap.put("private",26.00);

        //given
        Mockito.when(autoScoutComputeService.calculateAverageListingSellingPricePerSellerType()).thenReturn(givenMap);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/averageSellingPriceBySellerType")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.other").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.private").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dealer").exists());


    }

    @Test
    public void test_percentage_by_make() throws Exception {
        //given
        Map<String,Double> givenMap = new HashMap<>();
        givenMap.put("Toyota",24.00);
        givenMap.put("Mercedes-Benz",25.00);
        givenMap.put("Renault",26.00);

        Mockito.when(autoScoutComputeService.calculatePercentageDistributionByMake()).thenReturn(givenMap);


        mockMvc.perform(MockMvcRequestBuilders
                .get("/percentageByMake")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Toyota").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Mercedes-Benz").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Renault").exists());

    }

    @Test
    public void test_most_contacted_listing_by_month() throws Exception {

        List<List<String>> listOfListingDetails = new ArrayList<>();

        List<String> listingDetailsList1 = Arrays.asList("1061","Renault","5641","7000","21");
        List<String> listingDetailsList2 = Arrays.asList("1132","Mercedes-Benz","34490","7000","18");
        List<String> listingDetailsList3 = Arrays.asList("1077","BWM","5914","8500","17");
        listOfListingDetails.add(listingDetailsList1);
        listOfListingDetails.add(listingDetailsList2);
        listOfListingDetails.add(listingDetailsList3);

        Mockito.when(autoScoutComputeService.mostContactedListingByMonth(Month.JANUARY)).thenReturn(listOfListingDetails);

        Mockito.when(autoScoutCSVReader.readContacts()).thenReturn(createContacts());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/mostContactedListingByMonth/{month}","JANUARY")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());

    }

    @Test
    public void test_average_price_of_30Most_contacted() throws Exception {
        //given

        Mockito.when(autoScoutComputeService.averagePriceOf30PercentMostContacted()).thenReturn(25.00);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/averagePriceOf30MostContacted")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNumber());
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