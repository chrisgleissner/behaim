package com.github.behaim.utils;

import com.github.behaim.explorer.FieldContext;
import com.github.behaim.explorer.VisitationResult;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;

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

    public static FieldContext fieldContext(Field field) {
        FieldContext context = mock(FieldContext.class);
        when(context.getField()).thenReturn(field);
        return context;
    }
}
