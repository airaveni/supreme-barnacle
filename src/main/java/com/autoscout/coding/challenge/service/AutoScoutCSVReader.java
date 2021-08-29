package com.autoscout.coding.challenge.service;

import com.autoscout.coding.challenge.model.Contact;
import com.autoscout.coding.challenge.model.Listing;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toList;


/**
 *
 *  @author  Ashok Kumar Iraveni
 *
 */
@Service
@Slf4j
public class AutoScoutCSVReader {


    private CSVReader csvReader;


    public List<Listing> readListings() {

        List<Listing> listingList = null;
        try {
            csvReader = new CSVReader(new FileReader("src/main/resources/listings.csv"));
            List<String[]> listOfAllLines = csvReader.readAll();
            //skip meta data
            listOfAllLines.remove(0);
            listingList = listOfAllLines.stream()
                    .map(strings -> new Listing(parseLong(strings[0]), strings[1], parseLong(strings[2]), parseLong(strings[3]), strings[4]))
                    .collect(toList());

        } catch (CsvException | IOException e) {
            log.error("Error in reading listings csv file");

        } finally {
            try {
                csvReader.close();
            } catch (IOException e) {
                log.error("Error in closing CSVReader connection");
            }
        }
        return listingList;
    }


    public List<Contact> readContacts() {
        List<Contact> contactList = null;
        try {
            csvReader = new CSVReader(new FileReader("src/main/resources/contacts.csv"));
            List<String[]> listOfAllLines = csvReader.readAll();
            //skip meta data
            listOfAllLines.remove(0);
            contactList = listOfAllLines.stream()
                    .map(strings -> new Contact(parseLong(strings[0]), LocalDateTime.ofInstant(Instant.ofEpochMilli(parseLong(strings[1])), ZoneId.systemDefault())))
                    .collect(toList());

        } catch (CsvException | IOException e) {
            log.error("Error in reading contacts csv file");

        } finally {
            try {
                csvReader.close();
            } catch (IOException e) {
                log.error("Error in closing CSVReader connection");
            }
        }
        return contactList;
    }
}
