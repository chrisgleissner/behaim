package com.github.chrisgleissner.behaim.explorer;

import com.github.chrisgleissner.behaim.domain.Person;
import com.github.chrisgleissner.behaim.route.Route;
import com.github.chrisgleissner.behaim.utils.MockUtils;
import org.junit.Test;

import java.util.Date;

import static com.github.chrisgleissner.behaim.domain.ColorEnum.BLUE;
import static com.github.chrisgleissner.behaim.domain.ColorEnum.GREEN;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabien DUMINY
 */
public class ExplorerTest {
    @Test
    public void testExplore_withCycle_singlePerson() throws Exception {
        testExplore(true, true);
    }

    @Test
    public void testExplore_withCycle_twoPersons() throws Exception {
        testExplore(true, false);
    }

    @Test
    public void testExplore_withoutCycle() throws Exception {
        testExplore(false, false);
    }

    private void testExplore(boolean withCycle, boolean singlePerson) throws Exception {
        Person person1 = new Person("person1", ONE, new Date(), null, null, BLUE);
        Person person2 = new Person("person2", TEN, new Date(), person1, null, GREEN);
        if (withCycle) {
            person1.setManager(singlePerson ? person1 : person2);
        }
        Visitor visitor = MockUtils.visitor(person1, person2);

        Route route = new Explorer(visitor, Integer.MAX_VALUE).explore(person1);

        assertThat(route).isNotNull();
    }

}