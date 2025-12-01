package Day1;

public class SafeDial {
    private int iterator; // Tracks current number pointed to. Range is 0 to 99, inclusive. 100 values

    public SafeDial() {
        this.iterator = 50; // Starting position is 50
    }

    /***
     * @param direction Direction to move. Either "L" for left, or "R" for right.
     * @param distance Distance to spin the dial
     * @return True if dial ends on 0. False if not.
     */
    public boolean moveEndsOnZero(String direction, int distance) {
        int pos = this.iterator; // Temp variable to do math on without modifying current position

        // Do math for distance, based on direction to get new valid position
        if ("R".equalsIgnoreCase(direction)) { // Moving right and increasing value
            pos += distance;
            while (pos > 99) { // We passed the number cutoff. Loop until in valid range
                pos -= 100; // Get the new position relative to a full rotation
            }
        }
        else { // Moving left and decreasing value
            pos -= distance;
            while (pos < 0) { // We passed the number cutoff. Loop until in valid range
                pos += 100; // Get the new position relative to a full rotation
            }
        }

        // Update iterator to new position
        this.iterator = pos;

        // Return true if iterator is at 0. False if not
        return (this.iterator == 0);
    }

    /***
     * @param direction Direction to move. Either "L" for left, or "R" for right.
     * @param distance Distance to spin the dial
     * @return Number of times zero was passed during rotation
     */
    public int movePassesZero(String direction, int distance) {
        int pos = this.iterator; // Temp variable to do math on without modifying current position
        int timesPassedZero = 0;

        // Do math for distance, based on direction to get new valid position
        if ("R".equalsIgnoreCase(direction)) { // Moving right and increasing value
            while (distance > 0) { // Clicks remaining
                pos += 1; // Move 1 Right
                distance -= 1; // Decrease remaining clicks

                if (pos == 100) { // Landed on 0 this iteration
                    timesPassedZero += 1; // Increment zero counter
                    pos -= 100; // Reset to 0
                }
            }
        }
        else { // Moving left and decreasing value
            while (distance > 0) { // Clicks remaining
                pos -= 1; // Move 1 Left
                distance -= 1; // Decrease remaining clicks

                if (pos == 0) { // Landed on 0 this iteration
                    timesPassedZero += 1; // Increment zero counter
                }

                // Check for move past 0
                if (pos == -1) {
                    pos = 99; // Loop around
                }
            }
        }

        // Update iterator to new position
        this.iterator = pos;

        // Return number of times zero was passed during this rotation
        return timesPassedZero;
    }
}
