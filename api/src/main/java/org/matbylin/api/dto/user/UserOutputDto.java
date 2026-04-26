package org.matbylin.api.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
public class UserOutputDto {
    private Data data;
    private Support support;
    private Meta _meta;

    @Getter
    public static class Data {
        public int id;
        public String email;
        @JsonProperty("first_name")
        public String firstName;
        @JsonProperty("last_name")
        public String lastName;
        public String avatar;
    }

    @Getter
    public static class Support {
        public String url;
        public String text;
    }

    @Getter
    public static class Meta {
        @JsonProperty("powered_by")
        public String poweredBy;
        @JsonProperty("docs_url")
        public String docsUrl;
        @JsonProperty("upgrade_url")
        public String upgradeUrl;
        @JsonProperty("example_url")
        public String exampleUrl;
        public String variant;
        public String message;
        public Object cta;
        public String context;
    }
}
