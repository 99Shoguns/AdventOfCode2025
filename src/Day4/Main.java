package Day4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Read input from file
        List<String> rows = getDay4Input();

        int width = rows.getFirst().length(); // Size of the rows
        int depth = rows.size(); // Size of the columns

        // Convert rows of strings into 2-dimensional array of booleans. True means a roll is there. False means empty.
        boolean[][] rolls = getRollsPresent(rows, width, depth);

        // Run exercises
        // Part 1
        System.out.println("Max movable rolls: " + runPart1(rolls, width, depth));

        // Part 2
        System.out.println("Max total movable rolls: " + runPart2(rolls, width, depth));
    }

    static List<String> getDay4Input() {
        String filePath = "src/Day4/Day4Input.txt";
        List<String> rows = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine(); // Each line is a battery bank

                // Add line to List
                rows.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }

        return rows;
    }

    // Input expects a '.' for no paper roll, and a '@' for a paper roll being present
    static boolean[][] getRollsPresent(List<String> rows, int width, int depth) {
        if (rows.isEmpty() || width < 1 || depth < 1) {
            return new boolean[0][0];
        }

        boolean[][] output = new boolean[width][depth]; // A 2D array of width/depth defined by input sizes

        int rowCnt = 0;
        // Fill the array with values. True = paper roll exists there. False = no paper roll exists there.
        for (String row : rows) { // Iterate over each row
            for (int i = 0; i < width; i++) { // Iterate over each character in each row
                // If character being checked is a paper roll, that index is true. If not, it's false.
                output[i][rowCnt] = "@".compareTo(row.substring(i, i + 1)) == 0;
            }
            rowCnt += 1;
        }

        return output;
    }

    /*
        The forklifts can only access a roll of paper if there are fewer than four rolls of paper in the
        eight adjacent positions. This means than the roll in question is only valid if no more than 3 other
        rolls exist in the surrounding dots. No need to check grids without a roll for surrounding spaces.

        - Like a 3x3 grid where the roll in question is in the center.
            . . .
            . @ .
            . . .
        - The top and bottom rows would be a 3x2 grid with the roll in the middle in the respective direction.
            . @ .    <- TOP     . . .
            . . .   Bottom ->   . @ .
        - The left/right columns would be a 2x3 grid with the roll in the middle on the respective side.
            . .                         . .
            @ .     <- LEFT RIGHT ->    . @
            . .                         . .
        - The corners would be a 2x2 grid with the roll in the respective corner.
            @ .     <- TOP LEFT         . @         . .     <- BOTTOM LEFT      . .
            . .     TOP RIGHT ->        . .         @ .     BOTTOM RIGHT ->     . @

     */
    static int runPart1(boolean[][] rows, int width, int depth) {
        int countRolls = 0, range = 1;
        int countAdjacent;

        // Modified in loops each run. These will be index max/mins offsets from current paper roll for searching.
        int maxLeftOffset, maxRightOffset, maxUpOffset, maxDownOffset;

        // These values are the width/depth coordinate that is currently being checked around the main paper roll
        int currentWidthOffset, currentDepthOffset;

        for (int i = 0; i < depth; i++) { // For each row
            maxUpOffset = (i - range < 0) ? -i : -range; // i = 1, range = 2. Can't look up 2, so only look back up 1.
            maxDownOffset = (i + range > depth - 1) ? ((depth - 1) - i) : range; // i = 12, depth = 14 [0-13], range = 2. Can't look down 2, so only look down 1 (14 - 13).

            for (int j = 0; j < width; j++) { // For each element in the row
                // If there is no paper roll here, continue to the next space
                if (!rows[j][i]) {
                    continue;
                }

                maxLeftOffset = (j - range < 0) ? -j : -range; // i = 1, range = 2. Can't look left 2, so only look back left 1.
                maxRightOffset = (j + range >= width - 1) ? ((width - 1) - j) : range; // i = 12, width = 14 [0-13], range = 2. Can't look right 2, so only look right 1 (14 - 13).

                // Start looking in the top left corner (if available) and search the row first.
                //     Then move down a row (if available) and search that row from left to right.
                //     Stop looking after maxLookDown and maxLookRight have been reached and checked.
                //     Skip the search if the current adjacent coordinates are the same as the main paper roll's
                currentWidthOffset = maxLeftOffset; // If range is 1: these will be -1, 0, 1, assuming those are within range
                currentDepthOffset = maxUpOffset; // If range is 1: these will be -1, 0 1, assuming those are within range

                // Set/Reset count
                countAdjacent = 0;

                // Begin checking for and totalling adjacent paper rolls
                while (currentWidthOffset <= maxRightOffset && currentDepthOffset <= maxDownOffset) {
                    // Check if current coordinates are the same as the main paper roll. Don't check for adjacent if they are.
                    if (j + currentWidthOffset != j || i + currentDepthOffset != i) {
                        // If there is a paper roll at checked location, add it to total adjacent count
                        if (rows[j + currentWidthOffset][i + currentDepthOffset]) {
                            countAdjacent += 1;
                        }

                        // Exit this check for adjacent rolls if over threshold
                        if (countAdjacent > 3) {
                            break;
                        }
                    }

                    // Not at end of allowed left to right values
                    if (currentWidthOffset < maxRightOffset) {
                        currentWidthOffset += 1;
                    }
                    // If at end of allowed left to right values, but not top to bottom values
                    else if (currentWidthOffset == maxRightOffset && currentDepthOffset < maxDownOffset) {
                        currentWidthOffset = maxLeftOffset;
                        currentDepthOffset += 1;
                    }
                    // We hit the max right and left values. Break out of loop and check next paper roll
                    else {
                        currentWidthOffset += 1;
                        currentDepthOffset += 1;
                    }
                }

                // Check if this paper roll can be picked up and increment total rolls if it can
                if (countAdjacent <= 3) {
                    countRolls += 1;
                }
            }
        }

        return countRolls;
    }

    /*
        Same as part 1, but remove the rolls of paper identified as moveable after each loop. Continue until
            no more rolls of paper can be moved.
     */
    static int runPart2(boolean[][] rows, int width, int depth) {
        int countAllRolls = 0;
        int countAdjacent, countRolls;
        int range = 1;
        boolean[][] updatedRows = new boolean[width][depth];

        // Copy the original 2D array into the array to be updated
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < width; j++) {
                updatedRows[j][i] = rows[j][i];
            }
        }

        // Modified in loops each run. These will be index max/mins offsets from current paper roll for searching.
        int maxLeftOffset, maxRightOffset, maxUpOffset, maxDownOffset;

        // These values are the width/depth coordinate that is currently being checked around the main paper roll
        int currentWidthOffset, currentDepthOffset;

        // Loop until count for this loop is 0
        do {
            countRolls = 0;

            // Update 2D array to the new values
            for (int i = 0; i < depth; i++) {
                for (int j = 0; j < width; j++) {
                    rows[j][i] = updatedRows[j][i];
                }
            }

            for (int i = 0; i < depth; i++) { // For each row
                maxUpOffset = (i - range < 0) ? -i : -range; // i = 1, range = 2. Can't look up 2, so only look back up 1.
                maxDownOffset = (i + range > depth - 1) ? ((depth - 1) - i) : range; // i = 12, depth = 14 [0-13], range = 2. Can't look down 2, so only look down 1 (14 - 13).

                for (int j = 0; j < width; j++) { // For each element in the row
                    // If there is no paper roll here, continue to the next space
                    if (!rows[j][i]) {
                        continue;
                    }

                    maxLeftOffset = (j - range < 0) ? -j : -range; // i = 1, range = 2. Can't look left 2, so only look back left 1.
                    maxRightOffset = (j + range >= width - 1) ? ((width - 1) - j) : range; // i = 12, width = 14 [0-13], range = 2. Can't look right 2, so only look right 1 (14 - 13).

                    // Start looking in the top left corner (if available) and search the row first.
                    //     Then move down a row (if available) and search that row from left to right.
                    //     Stop looking after maxLookDown and maxLookRight have been reached and checked.
                    //     Skip the search if the current adjacent coordinates are the same as the main paper roll's
                    currentWidthOffset = maxLeftOffset; // If range is 1: these will be -1, 0, 1, assuming those are within range
                    currentDepthOffset = maxUpOffset; // If range is 1: these will be -1, 0 1, assuming those are within range

                    // Set/Reset count
                    countAdjacent = 0;

                    // Begin checking for and totalling adjacent paper rolls
                    while (currentWidthOffset <= maxRightOffset && currentDepthOffset <= maxDownOffset) {
                        // Check if current coordinates are the same as the main paper roll. Don't check for adjacent if they are.
                        if (j + currentWidthOffset != j || i + currentDepthOffset != i) {
                            // If there is a paper roll at checked location, add it to total adjacent count
                            if (rows[j + currentWidthOffset][i + currentDepthOffset]) {
                                countAdjacent += 1;
                            }

                            // Exit this check for adjacent rolls if over threshold
                            if (countAdjacent > 3) {
                                break;
                            }
                        }

                        // Not at end of allowed left to right values
                        if (currentWidthOffset < maxRightOffset) {
                            currentWidthOffset += 1;
                        }
                        // If at end of allowed left to right values, but not top to bottom values
                        else if (currentWidthOffset == maxRightOffset && currentDepthOffset < maxDownOffset) {
                            currentWidthOffset = maxLeftOffset;
                            currentDepthOffset += 1;
                        }
                        // We hit the max right and left values. Break out of loop and check next paper roll
                        else {
                            currentWidthOffset += 1;
                            currentDepthOffset += 1;
                        }
                    }

                    // Check if this paper roll can be picked up and increment total rolls if it can
                    if (countAdjacent <= 3) {
                        countRolls += 1;
                        updatedRows[j][i] = false; // Remove this paper roll for the next iteration
                    }
                }
            }

            countAllRolls += countRolls;
        } while (countRolls > 0);

        return countAllRolls;
    }
}
