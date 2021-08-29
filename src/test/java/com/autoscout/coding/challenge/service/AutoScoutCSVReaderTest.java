package com.autoscout.coding.challenge.service;

import com.autoscout.coding.challenge.model.Contact;
import com.autoscout.coding.challenge.model.Listing;
import com.opencsv.CSVReader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AutoScoutCSVReaderTest {

    @InjectMocks
    private AutoScoutCSVReader autoScoutCSVReader;

    @Test
    public void test_read_listings()  {
        List<Listing> listingsList =  autoScoutCSVReader.readListings();
        Assert.assertEquals(300, listingsList.size());
    }

    @Test
    public void test_read_contacts()  {
        List<Contact> contactList =  autoScoutCSVReader.readContacts();
        Assert.assertEquals(14095, contactList.size());
    }
}