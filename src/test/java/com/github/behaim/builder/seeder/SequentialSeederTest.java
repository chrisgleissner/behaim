package com.github.behaim.builder.seeder;

import org.junit.Test;

/**
 * @author Fabien DUMINY
 */
public class SequentialSeederTest extends AbstractSeederTest<SequentialSeeder> {
    @Override
    SequentialSeeder createSeeder() {
        return new SequentialSeeder(config);
    }

    @Test
    public void testCreateSeed() throws Exception {
        for (int i = 0; i < 5; i++) {
            int expected = MIN_VALUE + (i % (INTERVAL_SIZE + 1));
            softAssertions.assertThat(seeder.createSeed()).as("seed[%d]", i).isEqualTo(expected);
        }

        assertAll();
    }
}