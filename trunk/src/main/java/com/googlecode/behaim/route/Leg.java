package com.googlecode.behaim.route;

import com.googlecode.behaim.explorer.FieldContext;
import com.googlecode.behaim.explorer.FieldUtil;

/**
 * Leg of a {@link Route}.
 * 
 * @author Christian Gleissner
 */
public class Leg {

	public final static Leg RETURN_LEG = new Leg(null, LegType.RETURN);
	private final FieldContext fieldContext;
	private final LegType type;

	public Leg(FieldContext fieldContext, LegType legType) {
		this.fieldContext = fieldContext;
		type = legType;
	}

	public FieldContext getFieldContext() {
		return fieldContext;
	}

	public LegType getType() {
		return type;
	}

	@Override
	public String toString() {
		String s = null;
		if (fieldContext == null) {
			s = type.name();
		} else {
			s = type.name() + "/" + FieldUtil.getNameAndTypeFor(fieldContext.getField());
		}
		return s;
	}
}
