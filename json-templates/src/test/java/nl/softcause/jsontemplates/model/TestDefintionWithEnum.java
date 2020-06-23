package nl.softcause.jsontemplates.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.experimental.Delegate;

@Data
public class TestDefintionWithEnum {
    public String name;
    public TestEnum value;
    public TestEnum[] valueArray;
    public ListTestNum values;

    public static class ListTestNum implements List<TestEnum> {
        @Delegate
        private List<TestEnum> base = new ArrayList<>();

        public ListTestNum(TestEnum...values) {
            base.addAll(Arrays.asList(values));
        }

    }
}
