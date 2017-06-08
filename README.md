# Auth2factor

   *   This project was generated with JAVA.
   *   This is sample code for get Two-step authentication Google app for your products
      
#### Build and run
- JAVA 
- MAVEN 

#### Setting dependencies


```
        <dependency>
            <groupId>commons-codec</groupId>
            <artifactId>commons-codec</artifactId>
            <version>1.10</version>
        </dependency>
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>javase</artifactId>
            <version>3.3.0</version>
        </dependency>

```

#### Sample to create secret key
    
```
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        String secretKey = base32.encodeToString(bytes);
        
```

#### Sample to create otp code

```
        String key = secretKey.replace(" ", "").toUpperCase();
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(key);
        String hexKey = Hex.encodeHexString(bytes);
        long time = (System.currentTimeMillis() / 1000) / 30;
        String hexTime = Long.toHexString(time);
        Rfc6238.getInstance().generateTOTP(hexKey,hexTime,"6");  
```