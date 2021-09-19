package org.izumi.haze.util;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

public class RandomList<T> extends LinkedList<T> {
    protected final Random random = new SecureRandom();

    public RandomList() {
    }

    public RandomList(Collection<? extends T> c) {
        super(c);
    }

    public T getRandom() {
        return get(random.nextInt(size()));
    }

    /**
     * Retrieves and removes a random element of the list
     */
    public T extractRandom() {
        int index = random.nextInt(size());
        T element = get(index);
        remove(index);
        return element;
    }
}
