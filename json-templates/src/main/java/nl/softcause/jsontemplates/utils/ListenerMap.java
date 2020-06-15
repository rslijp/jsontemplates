package nl.softcause.jsontemplates.utils;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("NullableProblems")
public class ListenerMap<K, V> extends ListenerModel implements Map<K, V> {

    @SuppressWarnings("WeakerAccess")
    public static final String PROP_PUT = "put";

    @SuppressWarnings("WeakerAccess")
    public static final String REMOVE_PUT = "remove";

    private Map<K, V> delegate;

    public ListenerMap() {
        this(new LinkedHashMap<>());
    }

    public ListenerMap(Map<K, V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return delegate.entrySet();
    }

    @Override
    public V get(Object key) {
        return delegate.get(key);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return delegate.keySet();
    }

    @Override
    public V put(K key, V value) {
        V oldValue = delegate.put(key, value);
        firePropertyChange(PROP_PUT, oldValue == null ? null : new AbstractMap.SimpleEntry<>(key, oldValue),
                new AbstractMap.SimpleEntry<>(key, value));
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        delegate.putAll(m);
    }

    @Override
    public V remove(Object key) {
        V oldValue = delegate.remove(key);
        firePropertyChange(REMOVE_PUT, oldValue == null ? null : new AbstractMap.SimpleEntry<>(key, oldValue),
                null);
        return oldValue;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public Collection<V> values() {
        return delegate.values();
    }
}
