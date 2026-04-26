package org.matbylin.api.fixtures;

import net.datafaker.Faker;
import org.matbylin.core.faker.FakerProvider;

public class BaseDtoData {
    protected static Faker getFaker() {
        return FakerProvider.get();
    }
}
