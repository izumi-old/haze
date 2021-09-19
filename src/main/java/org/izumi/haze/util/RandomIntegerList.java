package org.izumi.haze.util;

public class RandomIntegerList extends RandomList<Integer> {

    /**
     * Will generate and add (howMuch) random integers into the list
     *
     * @param howMuch - the amount of integers have be generated
     * @param bound – the upper bound (exclusive). Must be positive.
     *
     * @return the list itself for chaining
     */
    public RandomIntegerList addRandomValuesChaining(int howMuch, int bound) {
        for (int i = 0; i < howMuch; i++) {
            Integer element = random.nextInt(bound);
            add(element);
        }

        return this;
    }

    public RandomIntegerList fillValuesRangeInclude(Range range) {
        for (int i = range.start; i < range.end; i++) {
            add(i);
        }

        return this;
    }

    public RandomIntegerList add(int i) {
        super.add(i);
        return this;
    }
}
