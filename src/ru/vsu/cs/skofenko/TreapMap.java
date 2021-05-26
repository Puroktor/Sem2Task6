package ru.vsu.cs.skofenko;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TreapMap<K extends Comparable<K>, V> implements Map<K, V> {
    private static class Entry<K extends Comparable<K>, V> implements Map.Entry<K, V>, Comparable<Entry<K, V>> {
        private final K key;
        private V value;

        @Override
        public int compareTo(Entry<K, V> o) {
            return key.compareTo(o.key);
        }

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V temp = this.value;
            this.value = value;
            return temp;
        }
    }

    private final Treap<Entry<K, V>> treap = new Treap<>();

    @Override
    public int size() {
        return treap.getSize();
    }

    @Override
    public boolean isEmpty() {
        return treap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return treap.contains(new Entry<>((K) key, null));
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry<K, V> entry : treap) {
            if (entry.value.equals(value))
                return true;
        }
        return false;
    }

    @Override
    public V get(Object key) {
        return treap.get(new Entry<>((K) key, null)).value;
    }

    @Override
    public V put(K key, V value) {
        var res = treap.put(new Entry<>(key, value));
        if (res != null)
            return res.getValue();
        return null;
    }

    public K findInOrderKey(int k) {
        var res = treap.findInOrderKey(k);
        if (res != null)
            return res.getKey();
        return null;
    }

    @Override
    public V remove(Object key) {
        var res = treap.remove(new Entry<>((K) key, null));
        if (res != null)
            return res.getValue();
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            treap.put(new Entry<>(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public void clear() {
        treap.clear();
    }

    @Override
    public Set<K> keySet() {
        return new DefaultNotSupportedSet<>() {
            final Iterator<Entry<K, V>> iterator = treap.iterator();

            @Override
            public int size() {
                return treap.getSize();
            }

            @Override
            public Iterator<K> iterator() {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public K next() {
                        return iterator.next().getKey();
                    }

                };
            }
            // TODO:
        };
    }

    @Override
    public Collection<V> values() {
        return new DefaultNotSupportedSet<>() {
            final Iterator<Entry<K, V>> iterator = treap.iterator();

            @Override
            public int size() {
                return treap.getSize();
            }

            @Override
            public Iterator<V> iterator() {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public V next() {
                        return iterator.next().getValue();
                    }

                };
            }
            // TODO:
        };
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new DefaultNotSupportedSet<>() {
            final Iterator<Entry<K, V>> iterator = treap.iterator();

            @Override
            public int size() {
                return treap.getSize();
            }

            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public Map.Entry<K, V> next() {
                        return iterator.next();
                    }

                };
            }
            // TODO:
        };
    }
}
