package org.izumi.haze.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RandomList<T> extends ArrayList<T> {
    private final Random random = new SecureRandom();

    public RandomList() {
    }

    public RandomList(Collection<? extends T> c) {
        super(c);
    }

    public T getRandom() {
        return get(random.nextInt(size()));
    }
}
