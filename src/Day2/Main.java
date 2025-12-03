package Day2;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
    Invalid IDs are any number combination that is repeated twice.
    Essentially split the number down the middle as a string and compare both sides to each other.
    If they are the same, the number is invalid.

    Examples: 11 -> 1 1, 22 -> 2 2, 1010 -> 10 10, 1188511885 -> 11885 11885 etc...
 */

public class Main {
    static List<SearchRange> searchRanges = new ArrayList<>();

    public static void main(String[] args) {
        List<BigInteger> invalidIds = new ArrayList<>();
        String testString, testFirst, testLast;
        BigInteger sumTotal = new BigInteger("0"); // Start with 0

        getDay2Input(); // Reads input file and populates searchRanges
        //verifyInputToConsole(); // This prints to console. Just need to verify numbers are there and no ERROR exists

        // Iterate through all search ranges
        for (SearchRange range : searchRanges) {
            // Loop from start number to end number
            for (BigInteger i = range.getStart(); i.compareTo(range.getEnd()) <= 0; i = i.add(BigInteger.ONE)) {
                testString = i.toString(); // Parse test value to string

                // Check if string can be split down the middle
                if (testString.length() % 2 == 0) {
                    testFirst = testString.substring(0, testString.length() / 2); // Split down the middle. Get first half
                    testLast = testString.substring(testString.length() / 2); // Split down the middle. Get second half

                    //System.out.println(testFirst);
                    //System.out.println(testLast);

                    // Id value is confirmed invalid (repeating values). Add to list of bad Ids
                    if (testFirst.compareTo(testLast) == 0) {
                        //System.out.println(testString);
                        invalidIds.add(new BigInteger(testString));
                    }
                }
            }
        }

        // Iterate through bad IDs list and sum up values
        for (BigInteger val : invalidIds) {
            sumTotal = sumTotal.add(val);
        }

        // Output final value
        System.out.println(sumTotal);
    }

    public static void getDay2Input() {
        String filePath = "src/Day2/Day2Input.txt";

        BigInteger start, end;

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine(); // This input file only has a single line

                // Parse out individual ranges
                String[] ranges = line.split(","); // should have "#-#" for each item in ranges now

                // Iterate through all the ranges
                for (String range : ranges) {
                    start = new BigInteger(range.split("-")[0]); // Left side of dash
                    end = new BigInteger(range.split("-")[1]); // Right side of dash

                    // Add to List
                    searchRanges.add(new SearchRange(start, end));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    public static void verifyInputToConsole() {
        for (SearchRange range : searchRanges) {
            if (range.getStart().compareTo(range.getEnd()) > 0) { // Start is larger than end
                System.out.println("ERROR start larger than end; Start: " + range.getStart() + "; End: " + range.getEnd());
            }
            else {
                System.out.println("Start: " + range.getStart() + "; End: " + range.getEnd());
            }
        }
    }
}
