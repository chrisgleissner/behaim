package com.github.behaim.builder.adapter;

import com.github.behaim.builder.config.FieldConfig;
import com.github.behaim.builder.seeder.RandomSeeder;
import com.github.behaim.builder.seeder.Seeder;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;

public class StringAdapterTest {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(StringAdapterTest.class);

    @Test
    public void testFactory() throws Exception {
        FieldConfig config = new FieldConfig();
        Seeder seeder = new RandomSeeder(config);
        StringAdapter stringFactory = new StringAdapter();
        for (int i = 0; i < 100; i++) {
            String value = stringFactory.convert(seeder);
            Assert.assertTrue(value.length() >= config.getMinLength());
            Assert.assertTrue(value.length() <= config.getMaxLength());
            logger.trace("String value: {}", value);
        }
    }
}
