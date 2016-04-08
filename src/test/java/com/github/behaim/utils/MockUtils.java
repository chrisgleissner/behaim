package com.github.behaim.utils;

import com.github.behaim.domain.Person;
import com.github.behaim.explorer.FieldContext;
import com.github.behaim.explorer.VisitationResult;
import com.github.behaim.explorer.Visitor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Fabien DUMINY
 */
public class MockUtils {
    public static Answer<VisitationResult> result(final FieldContext fieldContext, final Object object, final Object fieldValue) {
        return new Answer<VisitationResult>() {
            @Override
            public VisitationResult answer(InvocationOnMock invocationOnMock) throws Throwable {
                fieldContext.getField().set(object, fieldValue);
                return new VisitationResult(fieldContext, fieldValue, false);
            }
        };
    }

    public static FieldContext fieldContext(PersonFields field) {
        FieldContext context = mock(FieldContext.class);
        when(context.getField()).thenReturn(field.getField());
        return context;
    }

    public static Visitor visitor(Person... persons) {
        Visitor visitor = mock(Visitor.class);
        FieldContext[] contexts = new FieldContext[PersonFields.values().length];
        for (PersonFields field : PersonFields.values()) {
            contexts[field.ordinal()] = fieldContext(field);
        }
        for (PersonFields field : PersonFields.values()) {
            for (Person person : persons) {
                when(visitor.visit(eq(person), eq(field.getField()))).thenAnswer(result(contexts[field.ordinal()], person, null));
            }
        }
        return visitor;
    }
}
