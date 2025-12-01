package Day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static List<Instruction> instructions = new ArrayList<>();

    public static void main(String[] args) {
        getDay1Input();
        SafeDial dialEndZeros = new SafeDial();
        SafeDial dialPassesZero = new SafeDial();
        int endZeroCnt = 0;
        int passZeroCnt = 0;

        for (Instruction instruction : instructions) {
            // Move dial for each instruction. If True (ended on 0) increment zero counter.
            if (dialEndZeros.moveEndsOnZero(instruction.getDirection(), instruction.getDistance())) {
                endZeroCnt += 1;
            }
        }

        for (Instruction instruction : instructions) {
            // Move dial for each instruction. Increment passed 0 counter by number of times passed
            passZeroCnt += dialPassesZero.movePassesZero(instruction.getDirection(), instruction.getDistance());
        }

        System.out.println("Total times ended on 0: " + endZeroCnt); // Part 1 Solution
        System.out.println("Total times passed 0: " + passZeroCnt); // Part 2 Solution
    }

    public static void getDay1Input() {
        String filePath = "/Users/99shoguns/IdeaProjects/AdventOfCode2025/src/Day1/Day1Input.txt";

        String direction;
        int distance;

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Parse out direction ('L' or 'R') and distance (Integer)
                direction = line.substring(0,1); // Get first character
                distance = Integer.parseInt(line.substring(1));

                // Add to List
                instructions.add(new Instruction(direction, distance));
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
}

