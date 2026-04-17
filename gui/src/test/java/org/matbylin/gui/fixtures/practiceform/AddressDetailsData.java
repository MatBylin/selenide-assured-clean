package org.matbylin.gui.fixtures.practiceform;

import lombok.experimental.UtilityClass;
import org.matbylin.core.models.personalinfo.AddressDetailsModel;

@UtilityClass
public class AddressDetailsData {
    public static AddressDetailsModel valid() {
        return AddressDetailsModel.builder()
                .city("New York")
                .country("USA")
                .build();
    }
}
