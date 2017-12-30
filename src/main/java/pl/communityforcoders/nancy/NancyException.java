package pl.communityforcoders.nancy;

public class NancyException extends RuntimeException {

  public NancyException(String message) {
    super(message);
  }

  public NancyException(Throwable throwable) {
    super(throwable);
  }

  public NancyException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
