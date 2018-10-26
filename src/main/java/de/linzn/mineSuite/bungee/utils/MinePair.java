package de.linzn.mineSuite.bungee.utils;

public class MinePair<K, V> {
    private V value;
    private K key;

    public MinePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }
}
