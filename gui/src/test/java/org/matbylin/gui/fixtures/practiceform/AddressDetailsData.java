package org.matbylin.gui.fixtures.practiceform;

import lombok.experimental.UtilityClass;
import org.matbylin.core.faker.FakerProvider;
import org.matbylin.core.models.personalinfo.AddressDetailsModel;

@UtilityClass
public class AddressDetailsData {
    public static AddressDetailsModel valid() {
        return AddressDetailsModel.builder()
                .city(FakerProvider.get().address().city())
                .country("USA")
                .build();
    }
}
