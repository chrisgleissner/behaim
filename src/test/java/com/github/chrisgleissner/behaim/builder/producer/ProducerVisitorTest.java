package com.github.chrisgleissner.behaim.builder.producer;

import com.github.chrisgleissner.behaim.builder.adapter.EnumAdapter;
import com.github.chrisgleissner.behaim.builder.adapter.SeedAdapter;
import com.github.chrisgleissner.behaim.builder.config.Config;
import com.github.chrisgleissner.behaim.builder.seeder.Seeder;
import com.github.chrisgleissner.behaim.domain.ColorEnum;
import com.github.chrisgleissner.behaim.domain.Person;
import com.github.chrisgleissner.behaim.explorer.VisitationResult;
import com.github.chrisgleissner.behaim.utils.PersonFields;
import com.github.chrisgleissner.behaim.domain.SingleValueEnum;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Fabien DUMINY
 */
@RunWith(Theories.class)
public class ProducerVisitorTest {
    private ProducerVisitorContext visitorContext;
    private ProducerFieldContext fieldContext;
    private Seeder seeder;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        visitorContext = mock(ProducerVisitorContext.class);
        fieldContext = mock(ProducerFieldContext.class);
        seeder = mock(Seeder.class);

        when(fieldContext.getSeeder()).thenReturn(seeder);
        when(visitorContext.getConfig()).thenReturn(new Config());
    }

    @Theory
    public void testVisit_colorEnum(ColorEnum expectedColor) throws Exception {
        testVisit_enumField(PersonFields.PREFERRED_COLOR, expectedColor);
    }

    @Test
    public void testVisit_emptyEnum() throws Exception {
        testVisit_enumField(PersonFields.EMPTY_ENUM, null);
    }

    @Test
    public void testVisit_singleValueEnum() throws Exception {
        testVisit_enumField(PersonFields.SINGLE_VALUE_ENUM, SingleValueEnum.SINGLE_VALUE);
    }

    @SuppressWarnings("unchecked")
    private <E extends Enum<E>> void testVisit_enumField(PersonFields personField, E expectedValue) throws Exception {
        Field field = personField.getField();
        when(visitorContext.getFieldContextFor(eq(field))).thenReturn(fieldContext);
        Class<? extends Enum> enumClass = field.getType().asSubclass(Enum.class);
        when(visitorContext.getSeedAdapterFor(eq(field))).thenReturn((SeedAdapter) new EnumAdapter<>(enumClass));
        when(seeder.createIntSeed()).thenReturn((expectedValue == null) ? 0 : expectedValue.ordinal());
        Person person = new Person();
        ProducerVisitor visitor = new ProducerVisitor(visitorContext);
        Assertions.assertThat(person.getPreferredColor()).isNull();

        VisitationResult result = visitor.visit(person, field);

        assertThat(result).isNotNull();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(result.getFieldContext()).isSameAs(fieldContext);
        soft.assertThat(result.getValue()).as("result.value").isSameAs(expectedValue);
        soft.assertThat(person).extracting(field.getName()).containsExactly(expectedValue);
        soft.assertAll();
        if (enumClass.getEnumConstants().length > 1) {
            verify(seeder).createIntSeed();
        }
        verify(fieldContext).getSeeder();
        verify(visitorContext).getConfig();
        verify(visitorContext).getFieldContextFor(eq(field));
        verify(visitorContext).getSeedAdapterFor(eq(field));
        verifyNoMoreInteractions(visitorContext, fieldContext, seeder);
    }
}