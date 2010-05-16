package com.googlecode.behaim.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.behaim.Behaim;
import com.googlecode.behaim.domain.Person;

public class BuilderTest {

	private final static Logger logger = LoggerFactory.getLogger(BuilderTest.class);
	private final static int NUMBER_OF_INSTANCES = 2;
	private static final int RECURSION_DEPTH = 1;

	private void assertFullyPopulated(Person person, String breadcrumb, int level) {
		logger.trace("[{}] Checking {}", level, breadcrumb);
		assertNotNull(breadcrumb + ".annualSalary empty at level " + level, person.getAnnualSalary());
		assertNotNull(breadcrumb + ".birthday empty at level " + level, person.getBirthday());
		assertNotNull(breadcrumb + ".name empty at level " + level, person.getName());
		if (level > 0) {
			assertFullyPopulated(person.getManager(), breadcrumb + ".manager", level - 1);
			for (Person teamMember : person.getTeam()) {
				assertFullyPopulated(teamMember, breadcrumb + ".team", level - 1);
			}
		}
	}

	@Test
	public void testProduce() {
		Collection<Person> persons = Behaim.build(Person.class, NUMBER_OF_INSTANCES);
		assertEquals(NUMBER_OF_INSTANCES, persons.size());
		int index = 0;
		for (Person person : persons) {
			assertNotNull(person);
			// assertFullyPopulated(person, "person[" + index++ + "]", RECURSION_DEPTH);
		}
	}
}
