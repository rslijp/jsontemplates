package nl.softcause.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClientSearchScreenAction  {

    @NotNull
    @NotBlank
    private String dialogName;

    private String label;

    @NotNull
    private ClientSearchScreenColumnActionType actionType;

    @NotNull
    @NotBlank
    private String icon;

    @NotNull
    private Integer location;

    public ClientSearchScreenAction() {
        // required
    }


    public enum ClientSearchScreenColumnActionType {
        ROW("row"),
        SCREEN("screen");

        private final String value;

        ClientSearchScreenColumnActionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
