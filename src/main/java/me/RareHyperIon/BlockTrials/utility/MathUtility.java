package me.RareHyperIon.BlockTrials.utility;

import java.util.Random;

public final class MathUtility {

    private static final Random RANDOM = new Random();

    public static int nextInt(final int min, final int max) {
        if(min > max) throw new IllegalArgumentException("Minimum cannot be greater than maximum.");
        return RANDOM.nextInt((max - min) + 1) + min;
    }

}
