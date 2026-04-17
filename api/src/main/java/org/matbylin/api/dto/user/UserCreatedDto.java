package org.matbylin.api.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreatedDto {
    public String name;
    public String job;
    public String id;
    public Date createdAt;
}
