package zfg.core;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class maybe {

  /** A data type that may or may not contain a value */
  public static sealed interface Maybe<T> permits Some, None {}

  /** A data type that contains a value */
  public static final class Some<T> implements Maybe<T> {
    private final T value;
    private Some(final T value) { this.value = value; }
    public T value() { return value; }
  }

  /** A data type that does not contain a value */
  public static final class None<T> implements Maybe<T> {
    private None() {}
  }

  // None singleton
  private static final None none = new None();

  /** Returns a {@link Some} that contains the given value */
  public static <T> Some<T> some(final T value) {
    if (value == null) throw new IllegalArgumentException("Value cannot be null");
    return new Some<>(value);
  }

  /** Returns a {@link None} */
  public static <T> None<T> none() { return none; }
}
