package com.github.behaim.route;

import com.github.behaim.explorer.FieldContext;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabien Duminy
 */
public class RouteTest {
    private static final FieldContext FIELD_CONTEXT1 = new MockFieldContext();
    private static final FieldContext FIELD_CONTEXT2 = new MockFieldContext();

    @Test
    public void testAddLeg_sameContext_sameType() throws Exception {
        Route route = new Route();

        route.addLeg(FIELD_CONTEXT1, LegType.ITERATE_OVER_ARRAY);
        route.addLeg(FIELD_CONTEXT1, LegType.ITERATE_OVER_ARRAY);

        assertSameLegs(route, true);
    }

    @Test
    public void testAddLeg_diffContext_sameType() throws Exception {
        Route route = new Route();

        route.addLeg(FIELD_CONTEXT1, LegType.ITERATE_OVER_ARRAY);
        route.addLeg(FIELD_CONTEXT2, LegType.ITERATE_OVER_ARRAY);

        assertSameLegs(route, false);
    }

    @Test
    public void testAddLeg_diffContext_diffType() throws Exception {
        Route route = new Route();

        route.addLeg(FIELD_CONTEXT1, LegType.ITERATE_OVER_ARRAY);
        route.addLeg(FIELD_CONTEXT2, LegType.NORMAL);

        assertSameLegs(route, false);
    }

    @Test
    public void testAddLeg_sameContext_diffType() throws Exception {
        Route route = new Route();

        route.addLeg(FIELD_CONTEXT1, LegType.ITERATE_OVER_ARRAY);
        route.addLeg(FIELD_CONTEXT1, LegType.NORMAL);

        assertSameLegs(route, false);
    }

    @Test
    public void testAddLeg_nullContext_returnType() throws Exception {
        Route route = new Route();

        route.addLeg(null, LegType.RETURN);

        assertReturnLeg(route);
    }

    @Test
    public void testAddLeg_nonNullContext_returnType() throws Exception {
        Route route = new Route();

        route.addLeg(FIELD_CONTEXT1, LegType.RETURN);

        assertReturnLeg(route);
    }

    private void assertSameLegs(Route route, boolean expectSame) {
        assertThat(route.getLegs()).hasSize(2);
        Iterator<Leg> legIterator = route.getLegs().iterator();
        Leg leg1 = legIterator.next();
        Leg leg2 = legIterator.next();
        assertThat(leg1).isNotNull();
        assertThat(leg2).isNotNull();

        if (expectSame) {
            assertThat(leg1).isSameAs(leg2);
        } else {
            assertThat(leg1).isNotSameAs(leg2);
        }
    }

    private void assertReturnLeg(Route route) {
        assertThat(route.getLegs()).hasSize(1);
        Iterator<Leg> legIterator = route.getLegs().iterator();
        Leg leg1 = legIterator.next();
        assertThat(Leg.RETURN_LEG).isNotNull();
        assertThat(leg1).isSameAs(Leg.RETURN_LEG);
    }

    private static class MockFieldContext implements FieldContext {
        @Override
        public Field getField() {
            return null;
        }
    }
}