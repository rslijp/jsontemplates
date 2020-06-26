package nl.softcause.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.experimental.Delegate;

@Getter
public class ClientTemplateConfiguration {


    private ClientSearchScreensMap searchScreens;

    public ClientTemplateConfiguration(){
        searchScreens = new ClientSearchScreensMap();
    }

    public static class ClientSearchScreensMap implements Map<String, ClientSearchScreen> {
        @Delegate
        private Map<String, ClientSearchScreen> base = new LinkedHashMap<>();

        public List<ClientSearchScreen> valuesAsList(){
            return new ArrayList<>(base.values());
        }
    }

}
