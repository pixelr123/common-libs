package org.collections;

import java.util.Objects;

public final class Pair<K, V> {
  K key;
  V value;

  public Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public static <K, V> Pair<K, V> of(final K key, final V value) {
    Objects.requireNonNull(key);
    Objects.requireNonNull(value);
    return new Pair<>(key, value);
  }

  public boolean containKey(final K other) {
    return Objects.equals(key, other);
  }

  public boolean containValue(final V other) {
    return Objects.equals(value, other);
  }

  public boolean compareTo(final Pair<K, V> other) {
    Objects.requireNonNull(other);
    return Objects.equals(key, other.getKey()) && Objects.equals(value, other.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    return obj instanceof Pair<?, ?> p
            && Objects.equals(getKey(), p.getKey())
            && Objects.equals(getValue(), p.getValue());
  }

  @Override
  public String toString() {
    return "[" + getKey() + ',' + getValue() + ']';
  }
}
