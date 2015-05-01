package com.brahalla.ftpsmanager;

public class App {

  public static void main(String[] args) {
    //FtpsManager ftpsManager = new FtpsManager("/path/to/certificate", "password", "hostname", 21, "username", "password");
    FtpsManager ftpsManager = new FtpsManager();
    ftpsManager.connect();
  }

}
