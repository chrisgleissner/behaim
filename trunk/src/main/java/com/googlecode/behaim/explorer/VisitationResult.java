package com.googlecode.behaim.explorer;

public class VisitationResult {

	private final FieldContext fieldContext;
	private final Object value;
	private final boolean visitOfValueRequired;

	public VisitationResult(FieldContext fieldContext, Object value, boolean visitOfValueRequired) {
		super();
		this.fieldContext = fieldContext;
		this.value = value;
		this.visitOfValueRequired = visitOfValueRequired;
	}

	public FieldContext getFieldContext() {
		return fieldContext;
	}

	public Object getValue() {
		return value;
	}

	public boolean isVisitOfValueRequired() {
		return visitOfValueRequired;
	}

	@Override
	public String toString() {
		return FieldUtil.getNameAndTypeFor(fieldContext.getField()) + "=" + value;
	}

}
