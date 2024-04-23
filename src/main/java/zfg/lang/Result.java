package zfg.lang;

public sealed interface Result<V, E> {
  public record Val<V, E>(V value) implements Result<V, E> {}
  public record Err<V, E>(E error) implements Result<V, E> {}
  public static <T, E> Result<T, E> val(T value) { return new Val<>(value); }
  public static <T, E> Result<T, E> err(E error) { return new Err<>(error); }
}
