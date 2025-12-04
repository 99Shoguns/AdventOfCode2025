package Day3;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<String> banks;

        // Read input from file
        banks = getDay3Input();

        // Run exercises
        runPart1(banks); // 17087
        runPart2(banks); // 169019504359949
    }

    /*
        The batteries are arranged into banks; each line of digits in your input corresponds to a single bank of
        batteries. Within each bank, you need to turn on exactly two batteries; the joltage that the bank produces
        is equal to the number formed by the digits on the batteries you've turned on. For example, if you have a
        bank like 12345, and you turn on batteries 2 and 4, the bank would produce 24 jolts.
        (You cannot rearrange batteries.)

        You'll need to find the largest possible joltage each bank can produce. In the example:

        In 987654321111111, you can make the largest joltage possible, 98, by turning on the first two batteries.
        In 811111111111119, you can make the largest joltage possible by turning on the batteries labeled 8 and 9, producing 89 jolts.
        In 234234234234278, you can make 78 by turning on the last two batteries (marked 7 and 8).
        In 818181911112111, the largest joltage you can produce is 92.
     */
    static void runPart1(List<String> banks) {
        int total = 0;
        for (String bank : banks) {
            total += getMaxJolts(bank);
        }
        System.out.println("Max Jolts: " + total);
    }

    // Same as part 1, except 12 batteries need turned on instead of 2 now
    static void runPart2(List<String> banks) {
        BigInteger total = new BigInteger("0");
        for (String bank : banks) {
            total = total.add(getMaxBigJolts(bank));
        }
        System.out.println("Max Big Jolts: " + total);
    }

    static List<String> getDay3Input() {
        String filePath = "src/Day3/Day3Input.txt";
        List<String> batteryBanks = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine(); // Each line is a battery bank

                // Add line to List
                batteryBanks.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }

        return batteryBanks;
    }

    static int getMaxJolts(String bank) {
        String firstBattery, secondBattery;
        int firstIndex = -1, secondIndex = -1, tempMax;
        int currentMax = 0;

        for (int i = 0; i < bank.length()-1; i++) { // First battery (first battery through second-to-last battery
            firstBattery = bank.substring(i, i+1); // Get single value
            for (int j = i+1; j < bank.length(); j++) { // Second battery (must be after first battery)

                if (currentMax == 99) {break;} // Can't get more than 99. Stop checking this battery bank. Move to next.

                secondBattery = bank.substring(j, j+1); // Get second value

                tempMax = Integer.parseInt(firstBattery.concat(secondBattery));
                if (tempMax > currentMax) {
                    currentMax = tempMax;
                    firstIndex = i;
                    secondIndex = j;
                }
            }
        }

        //System.out.println("Bank: " + bank + ", Max Jolts: " + currentMax + " [" + firstIndex + "," + secondIndex + "]");

        return currentMax;
    }

    static BigInteger getMaxBigJolts(String bank) {
        int size = bank.length();
        // Both arrays will have indexes that match
        String[] values = {"0", "0", "0", "0", "0", "0",
            "0", "0", "0", "0", "0", "0"}; // Each index is a battery string value
        int[] indexes = {size-12, size-11, size-10, size-9, size-8, size-7,
            size-6, size-5, size-4, size-3, size-2, size-1}; // Index of values above at given index in array

        // Example: size = 15. The last value in indexes will be index 14, which is the 15th element in bank.
        // This is used to track where the last value is located.
        // In this example, the closest to the start that the last index can get is index 11, since there needs to be
        //  room for all the other battery indexes to fit.
        // In the example above, with size 15, the smallest value that index 11 can be is 11. It starts at the end though.

        String maxStr = "";
        String tempStr;
        int currentIndex = 0;

        int availableStart = 0; // How far from the front we need to start (Start looking at beginning)
        int endOffset = size-12; // How far from the end we need to stop (Stop looking with enough room for other batteries)

        // Start with the most significant value (first) and iterate to next significant value (2 -> 12)
        //   Set the next available start point (availableStart) to the value of indexes[i]. Starting with index[0] = 0
        //   Set last available battery to 1 less than next battery. If on last battery, then end is 1 less than size.
        while (endOffset < size) {
            for (int i = availableStart; i <= endOffset; i++) {
                tempStr = bank.substring(i, i+1); // Get value for comparison

                // Check if new value is greater than current value
                if (Integer.parseInt(tempStr) > Integer.parseInt(values[currentIndex])) {
                    values[currentIndex] = tempStr; // Update value
                    indexes[currentIndex] = i; // Update index where value was found in bank

                    if (tempStr.compareTo("9") == 0) {break;} // Max available value. Move to next battery
                }
            }

            maxStr = maxStr.concat(values[currentIndex]); // Add indexed values to final string

            if (currentIndex == 11) {break;} // Just finished last battery. Stop searching

            // Done checking for bigger values. Update values for next iteration
            availableStart = indexes[currentIndex] + 1; // Next available to search for is at the next index in bank
            endOffset = indexes[currentIndex+1]; // Set end point to index of next available battery. Can't look past there.
            currentIndex += 1; // Update to look for next battery's best value
        }

        String indexesStr = " [" + indexes[0] + "," + indexes[1] + "," + indexes[2] + ","
                + indexes[3] + "," + indexes[4] + "," + indexes[5] + ","
                + indexes[6] + "," + indexes[7] + "," + indexes[8] + ","
                + indexes[9] + "," + indexes[10] + "," + indexes[11] + "]";

        //System.out.println("Bank: " + bank + ", Max Jolts: " + maxStr + indexesStr);

        return new BigInteger(maxStr);
    }
}
