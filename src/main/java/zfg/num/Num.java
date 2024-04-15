package zfg.num;

public interface Num<T extends Num<T>> extends Lit {
  public T pos();
  public T neg();

  public T add(final T v);
  public T sub(final T v);
  public T mul(final T v);
  public T div(final T v);
  public T rem(final T v);
  public T mod(final T v);

  public String asString();
}
