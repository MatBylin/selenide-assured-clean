package org.matbylin.api.dto.user;

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
        public String first_name;
        public String last_name;
        public String avatar;
    }

    @Getter
    public static class Support {
        public String url;
        public String text;
    }

    @Getter
    public static class Meta {
        public String powered_by;
        public String docs_url;
        public String upgrade_url;
        public String example_url;
        public String variant;
        public String message;
        public Object cta;
        public String context;
    }
}
