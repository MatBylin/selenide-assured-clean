package org.matbylin.core.faker;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;

@UtilityClass
public final class FakerProvider {

    private static final ThreadLocal<Faker> FAKER = ThreadLocal.withInitial(Faker::new);

    public static Faker get() {
        return FAKER.get();
    }

    public static void remove() {
        FAKER.remove();
    }
}
