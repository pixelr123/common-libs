package org.common.collections;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;


public final class Present<T> {
  private final static Present<?> EMPTY = new Present<>(null);
  public T value;

  public Present(T value) {
    this.value = value;
  }

  @SuppressWarnings("unchecked")
  public static <T> Present<T> empty() {
    return (Present<T>) EMPTY;
  }

  public static <T> Present<T> of(T value) {
    return new Present<>(Objects.requireNonNull(value));
  }

  @SuppressWarnings("unchecked")
  public static <T> Present<T> OfNullable(T value) {
    return Objects.isNull(value) ? (Present<T>) EMPTY : new Present<>(value);
  }

  public T get() throws NoSuchFieldException {
    if (Objects.isNull(value)) {
      throw new NoSuchFieldException("Value is not present");
    }
    return value;
  }

  public boolean isPresent() {
    return value != null;
  }

  public boolean isEmpty() {
    return Objects.isNull(value);
  }

  public void ifPresent(Consumer<? super T> action) {
    if (value != null) {
      action.accept(value);
    }
  }

  public void ifPresentOrElse(Consumer<? super T> action, Runnable runnable) {
    if (value != null) {
      action.accept(value);
    } else {
      runnable.run();
    }
  }

  public Present<T> filter(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate);
    if (isEmpty()) {
      return this;
    } else {
      return predicate.test(value) ? this : empty();
    }
  }

  public <U> Present<U> map(Function<? super T, ? extends U> mapper) {
    Objects.requireNonNull(mapper);
    if (isEmpty()) return empty();
    return Present.OfNullable(mapper.apply(value));
  }

  @SuppressWarnings("unchecked")
  public <U> Present<U> flatMap(Function<? super T, ? extends Present<? extends U>> mapper) {
    Objects.requireNonNull(mapper);
    if (isEmpty()) return empty();
    Present<U> p = (Present<U>) mapper.apply(value);
    return Objects.requireNonNull(p);
  }

  public Stream<T> stream() {
    if (isEmpty()) return Stream.empty();
    return Stream.of(value);
  }

  public T orElseGet(Supplier<? extends T> supplier) {
    return value != null ? value : supplier.get();
  }

  public T orElse(T t) {
    return value != null ? value : t;
  }

  public T orElseThrow() {
    if (value == null) {
      throw new NoSuchElementException("No value present");
    }
    return value;
  }

  public <X extends Throwable> T orElseThrow(Supplier<? extends X> supplier) throws X {
    if (value != null) return value;
    throw supplier.get();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    return obj instanceof Present<?> p
            && Objects.equals(value, p.value);
  }

  @Override
  public String toString() {
    return value != null ? ("Present[" + value + "]")
            : "Present.empty";
  }
}