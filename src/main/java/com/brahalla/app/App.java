package com.brahalla.app;

public class App
{
    public static void main( String[] args )
    {
        FtpsManager ftpsManager = new FtpsManager("/path/to/certificate", "password", "hostname", "username", "password");
        ftpsManager.connect();
    }
}
