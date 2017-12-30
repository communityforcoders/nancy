package pl.communityforcoders.nancy.database;

public interface Database {

  void open();

  void close();

  boolean isRunning();

}
