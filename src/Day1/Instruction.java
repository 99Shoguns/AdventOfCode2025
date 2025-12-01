package Day1;

public class Instruction {
    private final String direction;
    private final int distance;

    public Instruction(String direction, int distance) {
        this.direction = direction;
        this.distance = distance;
    }

    public String getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }
}
