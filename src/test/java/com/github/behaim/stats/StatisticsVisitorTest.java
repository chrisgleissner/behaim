package com.github.behaim.stats;

import com.github.behaim.domain.Person;
import com.github.behaim.explorer.VisitationResult;
import com.github.behaim.utils.PersonFields;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.SoftAssertions;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.*;

import static com.github.behaim.stats.StatisticsVisitorTest.ValueType.NULL;
import static com.github.behaim.utils.PersonFields.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabien DUMINY
 */
@RunWith(Theories.class)
public class StatisticsVisitorTest {
    private static final Class<? extends Collection> EMPTY_LIST = Collections.emptyList().getClass();

    @Theory
    public void testConstructor(PersonFields field) throws Exception {
        StatisticsVisitor visitor = new StatisticsVisitor();

        assertThatCount(visitor, field).isEqualTo(0);
        assertThat(visitor.getClasses()).as("classes").isEmpty();
    }

    @Theory
    public void testVisit_nullObject(PersonFields field) throws Exception {
        StatisticsVisitor visitor = new StatisticsVisitor();

        VisitationResult result = visitor.visit(null, field.getField());

        assertResultIsValid(result, field, null);
        assertThatCount(visitor, field).isEqualTo(0);
        assertThat(visitor.getClasses()).as("classes").isEmpty();
    }

    @Theory
    public void testVisit_oneObject(PersonFields field, ValueType valueType) throws Exception {
        StatisticsVisitor visitor = new StatisticsVisitor();
        Person person1 = createPerson(field, NULL.equals(valueType) ? null : 1);

        VisitationResult result = visitor.visit(person1, field.getField());

        assertResultIsValid(result, field, person1);
        int expectedCount = expectedCount(field, valueType);
        assertThatCount(visitor, field).isEqualTo(expectedCount);
        assertThat(visitor.getClasses()).as("classes").containsExactly(expectedClasses(field, valueType));
    }

    @Theory
    public void testVisit_twoObjects(PersonFields field, ValueType valueType) throws Exception {
        StatisticsVisitor visitor = new StatisticsVisitor();
        Person person1 = createPerson(field, NULL.equals(valueType) ? null : 1);
        Person person2 = createPerson(field, NULL.equals(valueType) ? null : 2);

        visitor.visit(person1, field.getField());
        visitor.visit(person2, field.getField());

        int expectedCount = 2 * expectedCount(field, valueType);
        assertThatCount(visitor, field).isEqualTo(expectedCount);
        assertThat(visitor.getClasses()).as("classes").containsExactly(expectedClasses(field, valueType));
    }

    private static String[] expectedClasses(PersonFields field, ValueType valueType) {
        TreeSet<String> expectedClasses = new TreeSet<String>();
        expectedClasses.add(Person.class.getName());
        if (field.isVisitable(NULL.equals(valueType))) {
            expectedClasses.add(getConcreteClass(field).getName());
        }
        return expectedClasses.toArray(new String[expectedClasses.size()]);
    }

    private static int expectedCount(PersonFields field, ValueType valueType) {
        int result = Person.class.equals(field.getField().getType()) ? 1 : 0;
        if (field.isVisitable(NULL.equals(valueType))) {
            result++;
        }
        return result;
    }

    enum ValueType {
        NULL,
        NOT_NULL;
    }

    private static Person createPerson(PersonFields field, Integer value) {
        Person person = new Person();
        field.setValue(person, value);
        return person;
    }

    private static AbstractIntegerAssert<?> assertThatCount(StatisticsVisitor visitor, PersonFields field) {
        Class<?> fieldClass = getConcreteClass(field);
        return assertThat(visitor.getCount(fieldClass)).as("count(%s)", fieldClass.getSimpleName());
    }

    private static Class<?> getConcreteClass(PersonFields field) {
        Class<?> fieldClass = field.getField().getType();
        if (Collection.class.equals(fieldClass)) {
            fieldClass = EMPTY_LIST;
        }
        return fieldClass;
    }

    private static void assertResultIsValid(VisitationResult result, PersonFields field, Person person) {
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(result).as("result").isNotNull();
        if (result != null) {
            soft.assertThat(result.getValue()).as("result.value").isSameAs(field.getValue(person));
            soft.assertThat(result.getFieldContext()).as("result.fieldContext").isNotNull();
            if (result.getFieldContext() != null) {
                soft.assertThat(result.getFieldContext().getField()).as("result.fieldContext.field")
                        .isSameAs(field.getField());
            }
            List<PersonFields> notVisitables = Arrays.asList(NAME, ANNUALSALARY, BIRTHDAY, EMPTY_ENUM);
            soft.assertThat(result.isVisitOfValueRequired()).as("result.visitOfValueRequired")
                    .isEqualTo(!notVisitables.contains(field));
        }
        soft.assertAll();
    }
}