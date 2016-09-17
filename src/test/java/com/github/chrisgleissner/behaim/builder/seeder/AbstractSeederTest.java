package com.github.chrisgleissner.behaim.builder.seeder;

import com.github.chrisgleissner.behaim.builder.config.FieldConfig;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author Fabien DUMINY
 */
public abstract class AbstractSeederTest<T extends Seeder> {
    static final int MIN_VALUE = 1; // inclusive
    static final int MAX_VALUE = 4; // exclusive
    static final int INTERVAL_SIZE = MAX_VALUE - MIN_VALUE;
    private static final int NB_CONSTANT_VALUES = 10;

    AbstractSeederTest() {
    }

    FieldConfig config = new FieldConfig(false, MIN_VALUE, MAX_VALUE, 0, 0);

    SoftAssertions softAssertions;
    T seeder;

    @Before
    public void setUp() {
        config = spy(config);
        softAssertions = new SoftAssertions();
        seeder = createSeeder();
    }

    abstract public void testCreateSeed() throws Exception;

    abstract public void testCreateIntSeed() throws Exception;

    abstract T createSeeder();

    @Test
    public final void testCreateSeed_constantValue() {
        config = spy(new FieldConfig(false, MAX_VALUE, MAX_VALUE, 0, 0));
        for (int i = 0; i < NB_CONSTANT_VALUES; i++) {
            softAssertions.assertThat(createSeeder().createSeed()).isEqualTo(MAX_VALUE);
        }
        assertAll(NB_CONSTANT_VALUES, true);
    }

    @Test
    public final void testCreateIntSeed_constantValue() {
        config = spy(new FieldConfig(false, MAX_VALUE, MAX_VALUE, 0, 0));
        for (int i = 0; i < NB_CONSTANT_VALUES; i++) {
            softAssertions.assertThat(createSeeder().createIntSeed()).isEqualTo(MAX_VALUE);
        }
        assertAll(NB_CONSTANT_VALUES, true);
    }

    void assertAll(int nbIterations) {
        assertAll(nbIterations, false);
    }

    private void assertAll(int nbIterations, boolean constantValues) {
        softAssertions.assertAll();
        verify(config, times(nbIterations)).isConstantValue();
        verify(config, atLeastOnce()).getMinValue();
        if (!constantValues) {
            verify(config, times(nbIterations)).getMaxValue();
        }
        verifyNoMoreInteractions(config);
    }
}