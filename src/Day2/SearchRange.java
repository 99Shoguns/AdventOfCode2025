package Day2;

import java.math.BigInteger;

public class SearchRange {
    private BigInteger start;
    private BigInteger end;

    public SearchRange(BigInteger start, BigInteger end) {
        this.start = start;
        this.end = end;
    }

    public BigInteger getStart() {
        return start;
    }

    public BigInteger getEnd() {
        return end;
    }
}
