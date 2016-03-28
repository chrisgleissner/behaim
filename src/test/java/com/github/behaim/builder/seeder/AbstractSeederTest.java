package com.github.behaim.builder.seeder;

import com.github.behaim.builder.config.FieldConfig;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

/**
 * @author Fabien DUMINY
 */
public abstract class AbstractSeederTest<T extends Seeder> {
    static final int MIN_VALUE = 1; // inclusive
    static final int MAX_VALUE = 4; // exclusive
    static final int INTERVAL_SIZE = MAX_VALUE - MIN_VALUE;

    AbstractSeederTest() {
    }

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    FieldConfig config;

    SoftAssertions softAssertions;
    T seeder;

    @Before
    public void setUp() {
        when(config.getMinValue()).thenReturn(MIN_VALUE);
        when(config.getMaxValue()).thenReturn(MAX_VALUE);
        softAssertions = new SoftAssertions();
        seeder = createSeeder();
    }

    abstract public void testCreateSeed() throws Exception;

    abstract T createSeeder();

    @Test
    public final void testCreateSeed_singleValue() {
        when(config.getMinValue()).thenReturn(MAX_VALUE);
        for (int i = 0; i < 10; i++) {
            softAssertions.assertThat(createSeeder().createSeed()).isEqualTo(MAX_VALUE);
        }
        assertAll();
    }

    void assertAll() {
        softAssertions.assertAll();
        verify(config, atLeastOnce()).getMinValue();
        verify(config, atLeastOnce()).getMaxValue();
        verifyNoMoreInteractions(config);
    }
}