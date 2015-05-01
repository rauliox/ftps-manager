package com.brahalla.ftpsmanager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class FtpsManagerTest {

  private FtpsManager ftpsManager;

  @Before
  public void setUp() {
    this.ftpsManager = new FtpsManager();
  }

  /*@Test(expected=NullPointerException.class)
  public void testUninitializedFtpsManagerThrowsNullPointerExceptionOnConnect() {
    this.ftpsManager.connect();
  }*/

}
