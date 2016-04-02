package com.github.behaim.builder.producer;

import com.github.behaim.builder.adapter.EnumAdapter;
import com.github.behaim.builder.adapter.SeedAdapter;
import com.github.behaim.builder.config.Config;
import com.github.behaim.builder.config.FieldConfig;
import com.github.behaim.builder.seeder.RandomSeeder;
import com.github.behaim.builder.seeder.SequentialSeeder;
import com.github.behaim.domain.ColorEnum;
import com.github.behaim.explorer.FieldUtil;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static com.github.behaim.domain.PersonFields.PREFERRED_COLOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Fabien DUMINY
 */
@RunWith(Theories.class)
public class ProducerVisitorContextTest {
    @Theory
    public void getFieldContextFor_enumField(boolean random) throws Exception {
        FieldConfig fieldConfig = mock(FieldConfig.class);
        when(fieldConfig.isRandom()).thenReturn(random);
        Config config = mock(Config.class);
        String fieldName = FieldUtil.getNameFor(PREFERRED_COLOR);
        when(config.getFieldConfigFor(eq(fieldName))).thenReturn(fieldConfig);
        ProducerVisitorContext visitorContext = new ProducerVisitorContext(config);

        ProducerFieldContext fieldContext = visitorContext.getFieldContextFor(PREFERRED_COLOR);

        verify(config).getFieldConfigFor(eq(fieldName));
        verify(fieldConfig).isRandom();
        verifyNoMoreInteractions(config, fieldConfig);
        assertThat(fieldContext).as("result").isNotNull();
        assertThat(fieldContext.getField()).as("result.field").isSameAs(PREFERRED_COLOR);
        assertThat(fieldContext.getSeeder()).as("result.seeder").isInstanceOf(random ? RandomSeeder.class : SequentialSeeder.class);
        FieldConfig actualConfig = fieldContext.getSeeder().getConfig();
        assertThat(actualConfig).as("actualConfig").isNotNull().isNotSameAs(fieldConfig);
        assertThat(actualConfig.getMinValue()).as("actualConfig.minValue").isEqualTo(0);
        assertThat(actualConfig.getMaxValue()).as("actualConfig.maxValue").isEqualTo(ColorEnum.values().length);
        assertThat(actualConfig.isRandom()).as("actualConfig.random").isEqualTo(random);
    }

    @Test
    public void getSeedAdapterFor() throws Exception {
        Config config = mock(Config.class);
        ProducerVisitorContext visitorContext = new ProducerVisitorContext(config);

        SeedAdapter<?> seedAdapter = visitorContext.getSeedAdapterFor(PREFERRED_COLOR);

        assertThat(seedAdapter).as("result").isInstanceOf(EnumAdapter.class);
        EnumAdapter enumAdapter = (EnumAdapter) seedAdapter;
        assertThat(enumAdapter.getValueClass()).isSameAs(ColorEnum.class);
        verifyNoMoreInteractions(config);
    }
}