package org.common.strings;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Joiner {

  private final String delimiter;

  public Joiner(String delimiter) {
    this.delimiter = delimiter;
  }

  public Joiner(Joiner prototype) {
    this.delimiter = prototype.delimiter;
  }

  public static Joiner on(String delimiter) {
    return new Joiner(delimiter);
  }

  public static Joiner on(char delimiter) {
    return new Joiner(String.valueOf(delimiter));
  }

  public final <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
    return appendTo(appendable, parts.iterator());
  }

  public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
    Objects.requireNonNull(appendable);
    if (parts.hasNext()) {
      appendable.append((CharSequence) parts.next());
      while (parts.hasNext()) {
        appendable.append(delimiter);
        appendable.append((CharSequence) parts.next());
      }
    }
    return appendable;
  }

  public final <A extends Appendable> A appendTo(A appendable, Objects[] parts) throws IOException {
    return appendTo(appendable, Arrays.asList(parts));
  }

  public String join(Iterable<?> parts) throws IOException {
    return join(parts.iterator());
  }

  public String join(Iterator<?> parts) throws IOException {
    return appendTo(new StringBuilder(), parts).toString();
  }

  public String join(Object[] parts) throws IOException {
    return join(Arrays.asList(parts));
  }

  public Joiner skipNulls() {
    return new Joiner(this) {
      @Override
      public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
        while (parts.hasNext()) {
          Object part = parts.next();
          if(Objects.nonNull(part)) {
            appendable.append(Joiner.this.toString(part));
            break;
          }
        }
        while (parts.hasNext()) {
          Object part = parts.next();
          appendable.append(delimiter);
          appendable.append(Joiner.this.toString(part));
        }
        return appendable;
      }
    };
  }

  private CharSequence toString(Object part) {
    if(Objects.nonNull(part) && part instanceof CharSequence) {
      return (CharSequence) part;
    }
    return part.toString();
  }
}
