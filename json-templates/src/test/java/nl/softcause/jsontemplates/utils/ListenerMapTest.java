package nl.softcause.jsontemplates.utils;

import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyChangeListener;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Gil on 01/07/2017.
 */
public class ListenerMapTest {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private ListenerMap<String, String> map;

    @Before
    public void setUp() {
        map = new ListenerMap<>();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenPut_ShouldFireTrigger() {
        boolean[] fired = {false};
        Map.Entry<String, String>[] listenEntry = new Map.Entry[1];
        boolean[] checkNull = {true};
        PropertyChangeListener propertyChangeListener = evt -> {
            if (ListenerMap.PROP_PUT.equals(evt.getPropertyName())) {
                if(checkNull[0]) {
                    assertThat(evt.getOldValue(), is(nullValue()));
                }
                else {
                    Map.Entry<String, String> oldValue = (Map.Entry<String, String>) evt.getOldValue();
                    assertThat(oldValue.getKey(), is("k1"));
                    assertThat(oldValue.getValue(), is("v1"));
                }
                listenEntry[0] = (Map.Entry<String, String>) evt.getNewValue();
                fired[0] = true;
            }
        };
        map.addPropertyChangeListener(propertyChangeListener);
        map.put("k1", "v1");
        assertThat(fired[0], is(true));
        assertThat(listenEntry[0].getKey(), is("k1"));
        assertThat(listenEntry[0].getValue(), is("v1"));
        checkNull[0] = false;
        map.put("k1", "v2");
    }

    @Test
    public void whenRemove_ShouldNotFire() {
        boolean[] fired = {false};
        PropertyChangeListener propertyChangeListener = evt -> fired[0] = true;
        map.addPropertyChangeListener(propertyChangeListener);
        map.put("k1", "v1");
        assertThat(fired[0], is(true));
        fired[0] = false;
        map.removePropertyChangeListener(propertyChangeListener);
        map.put("k2", "v2");
        assertThat(fired[0], is(false));
    }

}