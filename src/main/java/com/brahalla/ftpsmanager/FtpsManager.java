package com.brahalla.ftpsmanager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.ftp.FTPSSocketFactory;

/*
 * Class to manage FTPS connections.
 *
 */
public class FtpsManager {

  private FTPSClient ftpsClient;

  private String keyStorePath;
  private String keyStorePassword;
  private String hostname;
  private Integer port;
  private String username;
  private String password;

  public FtpsManager(String keyStorePath, String keyStorePassword, String hostname, Integer port, String username, String password) {
    this.setKeyStorePath(keyStorePath);
    this.setKeyStorePassword(keyStorePassword);
    this.setHostname(hostname);
    this.setPort(port);
    this.setUsername(username);
    this.setPassword(password);
  }

  public FTPSClient getFtpsClient() {
    return this.ftpsClient;
  }

  private void setFtpsClient(FTPSClient ftpsClient) {
    this.ftpsClient = ftpsClient;
  }

  private String getKeyStorePath() {
    return this.keyStorePath;
  }

  private void setKeyStorePath(String keyStorePath) {
    this.keyStorePath = keyStorePath;
  }

  private String getKeyStorePassword() {
    return this.keyStorePassword;
  }

  private void setKeyStorePassword(String keyStorePassword) {
    this.keyStorePassword = keyStorePassword;
  }

  private String getHostname() {
    return this.hostname;
  }

  private void setHostname(String hostname) {
    this.hostname = hostname;
  }

  private Integer getPort() {
    return this.port;
  }

  private void setPort(Integer port) {
    this.port = port;
  }

  private String getUsername() {
    return this.username;
  }

  private void setUsername(String username) {
    this.username = username;
  }

  private String getPassword() {
    return this.password;
  }

  private void setPassword(String password) {
    this.password = password;
  }

  public void connect() {
    try {
      // Create FTPS client
      this.setFtpsClient(new FTPSClient("SSL", false));
      this.ftpsClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
      this.ftpsClient.setRemoteVerificationEnabled(false);

      // Configure network paramters
      SSLContext sslContext = this.createSslContext(this.getKeyStorePath(), this.getKeyStorePassword());
      FTPSSocketFactory ftpsSocketFactory = new FTPSSocketFactory(sslContext);
      this.ftpsClient.setSocketFactory(ftpsSocketFactory);
      this.ftpsClient.setBufferSize(1000);

      // Configure security certificate for ssl connection
      KeyManager keyManager = this.createKeyManagers(this.getKeyStorePath(), this.getKeyStorePassword())[0];
      TrustManager trustManager = this.createTrustManagers(this.getKeyStorePath(), this.getKeyStorePassword())[0];
      this.ftpsClient.setControlEncoding("UTF-8");
      this.ftpsClient.setKeyManager(keyManager);
      this.ftpsClient.setTrustManager(trustManager);

      // Connect to server
      this.ftpsClient.connect(this.getHostname(), this.getPort());

    } catch (Exception e){
      e.printStackTrace();
    }
  }

  private SSLContext createSslContext(String keyStorePath, String keyStorePassword) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, UnrecoverableKeyException, IOException {
    TrustManager[] trustManagers = this.createTrustManagers(keyStorePath, keyStorePassword);
    SSLContext sslContext = SSLContext.getInstance("SSLv3");
    sslContext.init(null, trustManagers, null);

    return sslContext;
  }

  private KeyManager[] createKeyManagers(String keyStorePath, String keyStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
    KeyStore keyStore = KeyStore.getInstance("JKS");
    keyStore.load(new FileInputStream(keyStorePath), keyStorePassword.toCharArray());
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

    return keyManagerFactory.getKeyManagers();
  }

  private TrustManager[] createTrustManagers(String keyStorePath, String keyStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
    KeyStore keyStore = KeyStore.getInstance("JKS");
    keyStore.load(new FileInputStream(keyStorePath), keyStorePassword.toCharArray());
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(keyStore);

    return trustManagerFactory.getTrustManagers();
  }

}
