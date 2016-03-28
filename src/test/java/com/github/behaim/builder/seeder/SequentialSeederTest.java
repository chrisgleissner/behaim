package com.github.behaim.builder.seeder;

import org.junit.Test;

/**
 * @author Fabien DUMINY
 */
public class SequentialSeederTest extends AbstractSeederTest<SequentialSeeder> {

    private static final int NB_ITERATIONS = 5;

    @Override
    SequentialSeeder createSeeder() {
        return new SequentialSeeder(config);
    }

    @Test
    public void testCreateSeed() throws Exception {
        for (int i = 0; i < NB_ITERATIONS; i++) {
            int expected = MIN_VALUE + (i % (INTERVAL_SIZE + 1));
            softAssertions.assertThat(seeder.createSeed()).as("seed[%d]", i).isEqualTo(expected);
        }

        assertAll(NB_ITERATIONS);
    }

    @Test
    public void testCreateIntSeed() throws Exception {
        for (int i = 0; i < NB_ITERATIONS; i++) {
            int expected = MIN_VALUE + (i % (INTERVAL_SIZE + 1));
            softAssertions.assertThat(seeder.createIntSeed()).as("seed[%d]", i).isEqualTo(expected);
        }

        assertAll(NB_ITERATIONS);
    }
}