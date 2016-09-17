package com.github.behaim.builder.seeder;

import org.junit.Test;

/**
 * @author Fabien DUMINY
 */
public class RandomSeederTest extends AbstractSeederTest<RandomSeeder> {

    private static final int NB_ITERATIONS = 1000;

    @Override
    RandomSeeder createSeeder() {
        return new RandomSeeder(config);
    }

    @Test
    public void testCreateSeed() throws Exception {
        for (int i = 0; i < NB_ITERATIONS; i++) {
            softAssertions.assertThat(seeder.createSeed()).as("seed[%d]", i).
                    isStrictlyBetween((double) MIN_VALUE - 1, (double) MAX_VALUE);
        }

        assertAll(NB_ITERATIONS);
    }

    @Test
    public void testCreateIntSeed() throws Exception {
        for (int i = 0; i < NB_ITERATIONS; i++) {
            softAssertions.assertThat(seeder.createIntSeed()).as("seed[%d]", i).
                    isStrictlyBetween(MIN_VALUE - 1, MAX_VALUE);
        }

        assertAll(NB_ITERATIONS);
    }
}