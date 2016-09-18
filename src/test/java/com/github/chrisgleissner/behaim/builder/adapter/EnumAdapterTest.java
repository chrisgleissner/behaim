package com.github.chrisgleissner.behaim.builder.adapter;

import com.github.chrisgleissner.behaim.builder.config.FieldConfig;
import com.github.chrisgleissner.behaim.builder.seeder.Seeder;
import com.github.chrisgleissner.behaim.domain.ColorEnum;
import com.github.chrisgleissner.behaim.domain.EmptyEnum;
import com.github.chrisgleissner.behaim.domain.SingleValueEnum;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Fabien DUMINY
 */
@RunWith(Theories.class)
public class EnumAdapterTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private Seeder seeder;
    @Mock
    private FieldConfig fieldConfig;

    @Theory
    public void testConvert(ColorEnum expectedColor) throws Exception {
        EnumAdapter<ColorEnum> adapter = createEnumAdapter(ColorEnum.class, expectedColor.ordinal());

        ColorEnum actualColor = adapter.convert(seeder);

        verifyActualValue(ColorEnum.class, actualColor, expectedColor);
    }

    @Test
    public void testConvert_emptyEnum() throws Exception {
        assertThat(EmptyEnum.values().length).isEqualTo(0);
        EnumAdapter<EmptyEnum> adapter = createEnumAdapter(EmptyEnum.class, 0);

        EmptyEnum actualValue = adapter.convert(seeder);

        verifyActualValue(EmptyEnum.class, actualValue, null);
    }

    @Test
    public void testConvert_singleValueEnum() throws Exception {
        Assertions.assertThat(SingleValueEnum.values().length).isEqualTo(1);
        EnumAdapter<SingleValueEnum> adapter = createEnumAdapter(SingleValueEnum.class, 0);

        SingleValueEnum actualValue = adapter.convert(seeder);

        verifyActualValue(SingleValueEnum.class, actualValue, SingleValueEnum.SINGLE_VALUE);
    }

    private <E extends Enum<E>> EnumAdapter<E> createEnumAdapter(Class<E> enumClass, int expectedOrdinal) {
        when(fieldConfig.getMinValue()).thenReturn(0);
        when(fieldConfig.getMaxValue()).thenReturn(ColorEnum.values().length);
        when(seeder.getConfig()).thenReturn(fieldConfig);
        when(seeder.createIntSeed()).thenReturn(expectedOrdinal);
        return new EnumAdapter<>(enumClass);
    }

    private <E extends Enum<E>> void verifyActualValue(Class<E> enumClass, E actualValue, E expectedValue) {
        assertThat(actualValue).isEqualTo(expectedValue);
        if (enumClass.getEnumConstants().length > 2) {
            verify(seeder).createIntSeed();
        }
        verifyNoMoreInteractions(seeder, fieldConfig);
    }
}