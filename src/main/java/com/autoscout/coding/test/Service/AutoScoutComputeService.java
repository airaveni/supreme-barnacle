package com.autoscout.coding.test.Service;

import com.autoscout.coding.test.model.Contact;
import com.autoscout.coding.test.model.Listing;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * @author Ashok Kumar Iraveni
 */

@Service
public class AutoScoutComputeService {

    @Autowired
    private AutoScoutCSVReader autoScoutCSVReader;

    /**
     * calculates average listing selling pricer per seller type
     *
     * @return
     */
    public Map<String, Double> calculateAverageListingSellingPricePerSellerType() {

        Map<String, Double> groupBySellerType = autoScoutCSVReader.readListings()
                .stream()
                .collect(groupingBy(Listing::getSellerType, averagingDouble(Listing::getPrice)));

        //reduce double to 2 decimals
        return groupBySellerType.entrySet()
                .stream()
                .map(entry -> {
                    Map<String, Double> averageDetails = new HashMap<>();
                    averageDetails.put(entry.getKey(), Precision.round(entry.getValue(), 2));
                    return averageDetails;
                })
                .reduce(new HashMap<>(), AutoScoutComputeService::reduceInto);
    }

    /**
     * calculates percentage distribution by make
     *
     * @return
     */
    public Map<String, Double> calculatePercentageDistributionByMake() {
        //group by make and count
        Map<String, Long> counts = autoScoutCSVReader.readListings()
                .stream()
                .collect(groupingBy(Listing::getMake, counting()));

        Long total = counts.values().stream().mapToLong(aLong -> aLong).sum();

        //calculate percentage
        Map<String, Double> unsortedMap = counts.entrySet()
                .stream()
                .collect(groupingBy(Map.Entry::getKey, Collectors.summingDouble(t -> (double) ((t.getValue() * 100) / total))));

        //sort and return
        return unsortedMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    }

    /**
     * calculates most contacted Listings by Month
     *
     * @param month @see "java.time.Month"
     * @return
     */
    public List<List<String>> mostContactedListingByMonth(Month month) {

        List<Contact> contactList = autoScoutCSVReader.readContacts();

        //filer based on month
        Map<Long, Long> contactsByMonth = contactList.stream().filter(e -> e.getContactDate().getMonth().equals(month))
                .collect(groupingBy(Contact::getListingId, counting()));

        //get Top5
        Map<Long, Long> top5Contacts = contactsByMonth.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        //get Listing info and return
        return top5Contacts.entrySet()
                .stream()
                .map(entry -> {
                    List<String> mappedData = new ArrayList<>();
                    Listing listing = getListingById(entry.getKey());
                    mappedData.add(String.valueOf(listing.getId()));
                    mappedData.add(listing.getMake());
                    mappedData.add(String.valueOf(listing.getPrice()));
                    mappedData.add(String.valueOf(listing.getMileage()));
                    mappedData.add(String.valueOf(entry.getValue()));
                    return mappedData;
                })
                .collect(Collectors.toList());

    }

    /**
     * calculates average price of 30 percent Most Contacted
     *
     * @return
     */
    public Double averagePriceOf30PercentMostContacted() {

        List<Contact> contactList = autoScoutCSVReader.readContacts();

        //groupBy ListingId and count
        Map<Long, Long> countByListing = contactList.stream()
                .collect(groupingBy(Contact::getListingId, counting()));

        long limitCount = (long) (countByListing.size() * 0.3);
        //minimum one listing
        if (limitCount == 0) {
            limitCount = 1;
        }

        //calculate 30 percent Listings
        Map<Long, Long> most30percentContacted = countByListing
                .entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(limitCount)//30 percent check
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        //mapping Listing data to Listing prices
        List<Long> priceList = most30percentContacted.entrySet()
                .stream()
                .map(entry -> {
                    Listing listing = getListingById(entry.getKey());
                    return listing.getPrice();
                })
                .collect(Collectors.toList());

        //calculate average and return
        return Precision.round(priceList.stream().mapToDouble(d -> d).average().orElse(0.0), 2);
    }

    /**
     * Responsible for providing Listing based on id
     *
     * @param id
     * @return
     */

    public Listing getListingById(long id) {
        return autoScoutCSVReader.readListings()
                .stream()
                .filter(listing -> listing.getId() == id)
                .findFirst()
                .get();
    }

    public static <R, T> Map<R, T> reduceInto(Map<R, T> into, Map<R, T> valuesToAdd) {
        into.putAll(valuesToAdd);
        return into;
    }

}
