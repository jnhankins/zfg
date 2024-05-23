package zfg;

public sealed interface Result<V, E> {

  public static final class Val<V, E> implements Result<V, E> {
    public final V val;
    public Val(final V val) { this.val = val; }
  }

  public static final class Err<V, E> implements Result<V, E> {
    public final E err;
    public Err(final E err) { this.err = err; }
  }

  public static <V, E> Result<V, E> val(final V val) { return new Val<>(val); }
  public static <V, E> Result<V, E> err(final E err) { return new Err<>(err); }
}
