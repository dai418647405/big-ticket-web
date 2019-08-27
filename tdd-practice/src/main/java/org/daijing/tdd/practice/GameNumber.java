package org.daijing.tdd.practice;

import static java.lang.String.valueOf;

public class GameNumber {
    private int rawNumber;

    public GameNumber(int rawNumber) {
        this.rawNumber = rawNumber;
    }

    @Override
    public String toString() {
        if (isRelatedTo(3) && isRelatedTo(5)) {
            return "fizzbuzz";
        }
        if (isRelatedTo(3)) {
            return "fizz";
        }
        if (isRelatedTo(5)) {
            return "buzz";
        }
        return valueOf(rawNumber);
    }

    private boolean isRelatedTo(int num) {
        return rawNumber % num == 0 || valueOf(rawNumber).contains(valueOf(num));
    }
}
