package com.googlecode.behaim.route;

import java.util.ArrayList;
import java.util.Collection;

import com.googlecode.behaim.explorer.Explorer;
import com.googlecode.behaim.explorer.Visitor;

/**
 * Route discovered by an {@link Explorer}.
 * 
 * @author Christian Gleissner
 */
public class Route {

	private final Collection<Leg> legs = new ArrayList<Leg>();

	public void add(Leg leg) {
		legs.add(leg);
	}

	private void append(StringBuilder sb, String s, int recursionLevel) {
		for (int i = 0; i < recursionLevel; i++) {
			sb.append("  ");
		}
		sb.append(s);
		sb.append("\n");
	}

	public Collection<Leg> getLegs() {
		return legs;
	}

	public Trip prepareTrip(Object startingLocation, Visitor visitor) {
		return new Trip(this, startingLocation, visitor);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(4096);
		int recursionLevel = 0;
		for (Leg leg : legs) {
			switch (leg.getType()) {
			case ITERATE_OVER_ARRAY:
				append(sb, "[]", recursionLevel);
				break;
			case ITERATE_OVER_COLLECTION:
				append(sb, "collection", recursionLevel);
				break;
			case RECURSE:
				recursionLevel++;
				append(sb, leg.toString(), recursionLevel);
				break;
			case NORMAL:
				append(sb, leg.toString(), recursionLevel);
				break;
			case RETURN:
				recursionLevel--;
				break;
			}
		}

		return sb.toString();
	}
}
