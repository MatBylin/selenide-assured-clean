package org.matbylin.api.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInputDto {
    private String name;
    private String job;
}
