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
        getDay2Input(); // Read from file and get number ranges

        // Run calculations and output results
        runPart1(); // 23701357374
        runPart2(); // 34284458938
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

    public static void runPart1() {
        List<BigInteger> invalidIds = new ArrayList<>();
        String testString, testFirst, testLast;
        BigInteger sumTotal = new BigInteger("0"); // Start with 0

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
        System.out.println("Part 1 total: " + sumTotal);
    }

    public static void runPart2() {
        List<BigInteger> invalidIds = new ArrayList<>();
        String testString, testPart;
        boolean isNotEqual = false;
        List<String> numberParts = new ArrayList<>();
        BigInteger sumTotal = new BigInteger("0"); // Start with 0

        // Iterate through all search ranges
        for (SearchRange range : searchRanges) {
            // Loop from start number to end number
            for (BigInteger i = range.getStart(); i.compareTo(range.getEnd()) <= 0; i = i.add(BigInteger.ONE)) {
                testString = i.toString(); // Parse test value to string

                // Iterate through all potential combinations of at least 2 numbers that can repeat
                for (int j = testString.length() / 2; j > 0; j--) {
                    // Get potential test part
                    testPart = testString.substring(0,j);

                    // Check if substring size can duplicate evenly throughout test string
                    if (testString.length() % testPart.length() != 0) {
                        continue; // Test part does not go into test string evenly
                    }

                    // Test part length goes into test string length evenly. We can compare sizes now.
                    for (int k = testPart.length() + testPart.length(); k <= testString.length(); k += testPart.length()) {
                        // Get all parts of the main number that are the same length as the test part
                        // Increment by size of the test part and grab the next section

                        // Example: size = 6. testPart = 2. k = 4 (2 + 2). Get substring 2 (4-2) to 4. [2,3]
                        //   Increment. k = 6 (4 + 2). Get substring 4 (6 - 2) to 6. [4,5]
                        numberParts.add(testString.substring(k - testPart.length(), k));
                    }

                    // Iterate through all number parts. If the test part matches all of them, it's an invalid Id
                    for (String part : numberParts) {
                        if (testPart.compareTo(part) != 0) { // Values do not match
                            isNotEqual = true;
                            break; // Indicate values don't match. Stop checking the rest of the parts
                        }
                    }

                    // testPart matches all other parts of the string. Add to list of bad Ids
                    if (!isNotEqual) {
                        invalidIds.add(new BigInteger(testString));
                        break; // This test string is invalid. Continue to next test string
                    }

                    // Clean up values for next loop
                    isNotEqual = false;
                    numberParts.clear();
                }
            }
        }

        // Iterate through bad IDs list and sum up values
        for (BigInteger val : invalidIds) {
            sumTotal = sumTotal.add(val);
        }

        // Output final value
        System.out.println("Part 2 total: " + sumTotal);
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
