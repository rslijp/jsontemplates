package nl.softcause.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import nl.softcause.jsontemplates.collections.StringMap;

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

    private StringMap postParams;

    public ClientSearchScreenAction() {
        // required
    }

    public void addParam(String key, String value) {
        if(postParams==null) postParams = new StringMap();
        postParams.put(key, value);
    }


    public enum ClientSearchScreenColumnActionType {
        ROW("row", true),
        ROWPOST("row-post", false),
        SCREEN("screen", true);

        private final String value;
        private final boolean openDialog;

        ClientSearchScreenColumnActionType(String value, boolean openDialog) {
            this.value = value;
            this.openDialog = openDialog;
        }

        public String getValue() {
            return this.value;
        }

        public boolean isOpenDialog(){
            return this.openDialog;
        }
    }
}
