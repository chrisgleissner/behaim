package com.github.behaim.builder.seeder;

import org.junit.Test;

/**
 * @author Fabien DUMINY
 */
public class RandomSeederTest extends AbstractSeederTest<RandomSeeder> {
    @Override
    RandomSeeder createSeeder() {
        return new RandomSeeder(config);
    }

    @Test
    public void testCreateSeed() throws Exception {
        for (int i = 0; i < 1000; i++) {
            softAssertions.assertThat(seeder.createSeed()).as("seed[%d]", i).
                    isStrictlyBetween((double) MIN_VALUE - 1, (double) MAX_VALUE);
        }

        assertAll();
    }
}