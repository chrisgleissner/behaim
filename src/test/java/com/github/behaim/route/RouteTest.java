package com.github.behaim.route;

import com.github.behaim.domain.Person;
import com.github.behaim.explorer.FieldContext;
import com.github.behaim.explorer.Visitor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

import static com.github.behaim.route.LegType.*;
import static com.github.behaim.utils.MockUtils.fieldContext;
import static com.github.behaim.utils.MockUtils.result;
import static com.github.behaim.utils.PersonFields.NAME;
import static com.github.behaim.utils.PersonFields.TEAM;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Fabien Duminy
 */
public class RouteTest {
    private static final String NAME_VALUE = NAME.getField().getName() + "Value";
    private static final Collection TEAM_VALUE = unmodifiableList(emptyList());
    private FieldContext nameContext;
    private FieldContext teamContext;
    @Mock private FieldContext collectionContext;

    @Before
    public void setUp() throws NoSuchFieldException {
        MockitoAnnotations.initMocks(this);
        nameContext = fieldContext(NAME);
        teamContext = fieldContext(TEAM);
    }

    @Test
    public void testAddLeg_sameContext_sameType() throws Exception {
        Route route = new Route();

        route.addLeg(nameContext, LegType.ITERATE_OVER_ARRAY);
        route.addLeg(nameContext, LegType.ITERATE_OVER_ARRAY);

        assertSameLegs(route, true);
    }

    @Test
    public void testAddLeg_diffContext_sameType() throws Exception {
        Route route = new Route();

        route.addLeg(nameContext, LegType.ITERATE_OVER_ARRAY);
        route.addLeg(teamContext, LegType.ITERATE_OVER_ARRAY);

        assertSameLegs(route, false);
    }

    @Test
    public void testAddLeg_diffContext_diffType() throws Exception {
        Route route = new Route();

        route.addLeg(nameContext, LegType.ITERATE_OVER_ARRAY);
        route.addLeg(teamContext, NORMAL);

        assertSameLegs(route, false);
    }

    @Test
    public void testAddLeg_sameContext_diffType() throws Exception {
        Route route = new Route();

        route.addLeg(nameContext, LegType.ITERATE_OVER_ARRAY);
        route.addLeg(nameContext, NORMAL);

        assertSameLegs(route, false);
    }

    @Test
    public void testAddLeg_nullContext_returnType() throws Exception {
        Route route = new Route();

        route.addLeg(null, RETURN);

        assertReturnLeg(route);
    }

    @Test
    public void testAddLeg_nonNullContext_returnType() throws Exception {
        Route route = new Route();

        route.addLeg(nameContext, RETURN);

        assertReturnLeg(route);
    }

    @Test
    public void testPrepareTrip_normal() throws Exception {
        testPrepareTrip(new Person(), nameContext, NORMAL, NAME_VALUE, false);
    }

    @Test
    public void testPrepareTrip_recurse() throws Exception {
        testPrepareTrip(new Person(), teamContext, RECURSE, TEAM_VALUE, false);
    }

    @Test
    public void testPrepareTrip_iterateOverCollection() throws Exception {
        testPrepareTrip(singletonList(new Person()), collectionContext, ITERATE_OVER_COLLECTION, TEAM_VALUE, true);
    }

    @Test
    public void testPrepareTrip_iterateOverArray() throws Exception {
        testPrepareTrip(new Person[]{new Person()}, collectionContext, ITERATE_OVER_ARRAY, TEAM_VALUE, true);
    }

    @Test
    public void testPrepareTrip_return() throws Exception {
        testPrepareTrip(new Person[]{new Person()}, collectionContext, RETURN, TEAM_VALUE, true);
    }

    private <T> void testPrepareTrip(Object object, FieldContext fieldContext, LegType legType, T expectedFieldValue, boolean expectNoVisit) throws Exception {
        Field field = fieldContext.getField();
        Route route = new Route();
        route.addLeg(fieldContext, legType);
        Visitor visitor = mock(Visitor.class);
        when(visitor.visit(eq(object), eq(field))).thenAnswer(result(fieldContext, object, expectedFieldValue));

        route.prepareTrip(object, visitor).perform();

        if (expectNoVisit) {
            verifyNoMoreInteractions(visitor);
        } else {
            verify(visitor).visit(eq(object), eq(field));
            assertThat(field.get(object)).as("person.%s", field.getName()).isEqualTo(expectedFieldValue);
        }
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
}