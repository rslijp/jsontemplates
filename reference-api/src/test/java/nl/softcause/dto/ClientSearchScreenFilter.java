package nl.softcause.dto;

import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClientSearchScreenFilter {

    @NotNull
    @NotBlank
    private String field;

    private String label;

    @NotNull
    private ClientSearchScreenFilterType inputType;

    @NotNull
    private Integer location;


    public ClientSearchScreenFilter() {
        // required
    }

    public enum ClientSearchScreenFilterType {
        TEXT("text", "String", "Integer"),
        RADIO("radio", "String", "Integer", "Boolean", "enum"),
        SELECT("select", "String", "Integer", "Boolean", "enum"),
        DATETIME("datetime", "DateRange");

        private final String value;
        private final List<String> types;

        ClientSearchScreenFilterType(String value, String... types) {
            this.value = value;
            this.types = Arrays.asList(types);
        }

        public String getValue() {
            return this.value;
        }

        public boolean supports(final String type) {
            return this.types.contains(type);
        }
    }
}
