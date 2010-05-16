package com.googlecode.behaim.explorer;

import java.lang.reflect.Field;

public interface Visitor {

	/**
	 * Visits a {@code field} of the specified {@code object}.
	 * 
	 * @param object
	 * @param field
	 * @return object with which the specified {@code Field} was filled or {@code null} if the field wasn't filled
	 */
	VisitationResult visit(Object object, Field field);
}
