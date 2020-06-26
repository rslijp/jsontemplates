package nl.softcause.dto;

import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClientSearchScreenColumn {

    @NotNull
    @NotBlank
    private String field;

    private String label;

    @NotNull
    private ClientSearchScreenColumnDataType dataType;

    @NotNull
    private DefaultSortDataType sort;

    @NotNull
    private Integer location;

    public ClientSearchScreenColumn() {
    }


    public enum ClientSearchScreenColumnDataType {
        TIMESTAMP("timestamp", "Instant"),
        DATE("date", "Instant"),
        DATETIME("datetime", "Instant"),
        ICON("icon", "boolean", "Boolean", "enum"),
        ENUM("enum", "boolean", "Boolean", "enum"),
        ICONENUM("iconenum", "boolean", "Boolean", "enum"),
        TEXT("text", "String", "List", "Integer", "int", "Long", "long", "boolean", "Boolean", "enum"),
        FLEXTEXT("flex-text", "String", "List"),
        MARKDOWN("markdown", "String"),
        NUMBER("number", "Integer");

        private final String value;
        private final List<String> types;

        ClientSearchScreenColumnDataType(String value, String... types) {
            this.value = value;
            this.types = Arrays.asList(types);

        }

        public static ClientSearchScreenColumnDataType[] canSupport(String type) {
            return Arrays.stream(ClientSearchScreenColumnDataType.values()).filter(v -> v.supports(type))
                    .toArray(ClientSearchScreenColumnDataType[]::new);
        }

        public String getValue() {
            return this.value;
        }

        public boolean supports(final String type) {
            return this.types.contains(type);
        }
    }

    public enum DefaultSortDataType {
        NO("no"),
        ASC("asc"),
        DESC("desc"),
        USER("user");

        private final String value;

        DefaultSortDataType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
