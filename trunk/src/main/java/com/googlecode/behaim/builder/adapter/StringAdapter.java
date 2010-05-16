package com.googlecode.behaim.builder.adapter;

import java.util.Random;

import com.googlecode.behaim.builder.config.FieldConfig;
import com.googlecode.behaim.builder.seeder.Seeder;

public class StringAdapter extends AbstractSeedAdapter<String> {

	private final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private final Random random = new Random();

	@Override
	public String convert(Seeder seeder) {
		FieldConfig fieldConfig = seeder.getConfig();
		int length = random.nextInt(fieldConfig.getMaxLength() - fieldConfig.getMinLength()) + fieldConfig.getMinLength();
		StringBuilder sb = new StringBuilder(length);
		long seed = (long) seeder.createSeed();
		if (seed < 0) {
			seed *= -1;
		}
		long initialSeed = seed;
		for (int i = 0; i < length; i++) {
			int charIndex = (int) (seed % CHARS.length());
			sb.append(CHARS.charAt(charIndex));
			seed /= CHARS.length();
			if (seed == 0) {
				seed = initialSeed;
			}
		}
		return sb.toString();
	}

	@Override
	public Class<String> getValueClass() {
		return String.class;
	}
}
