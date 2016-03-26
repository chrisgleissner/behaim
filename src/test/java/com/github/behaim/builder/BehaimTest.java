package com.github.behaim.builder;

import com.github.behaim.Behaim;
import com.github.behaim.builder.config.Config;
import com.github.behaim.domain.Person;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class BehaimTest {
    private final static Logger logger = LoggerFactory.getLogger(BehaimTest.class);
    private static final Config CONFIG = new Config(0, 1);

    @Test
    public void testBuild_1Param_oneInstance() {
        Person person = Behaim.build(Person.class);

        SoftAssertions assertions = new SoftAssertions();
        assertFullyPopulated(assertions, person, "person", CONFIG.getRecursionDepth());
        assertions.assertAll();
    }

    @Test
    public void testBuild_2Params_oneInstance() {
        Collection<Person> persons = Behaim.build(Person.class, 1);

        assertResult(persons, 1);
    }

    @Test
    @Ignore(value = "broken test") //TODO fix the bug
    public void testBuild_2Params_twoInstances() {
        Collection<Person> persons = Behaim.build(Person.class, 2);

        assertResult(persons, 2);
    }

    private void assertResult(Collection<Person> persons, int expectedNumberOfInstances) {
        assertThat(persons).as("persons").hasSize(expectedNumberOfInstances);
        SoftAssertions assertions = new SoftAssertions();
        int index = 0;
        for (Person person : persons) {
            assertFullyPopulated(assertions, person, "person[" + index++ + "]", CONFIG.getRecursionDepth());
        }
        assertions.assertAll();
    }

    private void assertFullyPopulated(SoftAssertions assertions, Person person, String breadcrumb, int level) {
        logger.trace("[{}] Checking {}", level, breadcrumb);
        assertions.assertThat(person).as(breadcrumb + " at level " + level).isNotNull();
        if (person == null) {
            return;
        }
        assertions.assertThat(person.getAnnualSalary()).as(breadcrumb + ".annualSalary at level " + level).isNotNull();
        assertions.assertThat(person.getBirthday()).as(breadcrumb + ".birthday at level " + level).isNotNull();
        assertions.assertThat(person.getName()).as(breadcrumb + ".name at level " + level).isNotNull();
        if (level > 0) {
            assertFullyPopulated(assertions, person.getManager(), breadcrumb + ".manager", level - 1);

            String teamProperty = breadcrumb + ".team at level " + level;
            assertions.assertThat(person.getTeam()).as(teamProperty).isNotNull();
            if (person.getTeam() != null) {
                for (Person teamMember : person.getTeam()) {
                    assertFullyPopulated(assertions, teamMember, teamProperty, level - 1);
                }
            }
        }
    }
}
