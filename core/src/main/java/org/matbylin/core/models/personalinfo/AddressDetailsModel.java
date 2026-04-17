package org.matbylin.core.models.personalinfo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
public class AddressDetailsModel {
    private String country;
    private String city;
}
