package th.co.nu.auth2factor.repository;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 6/6/2560.
 */

public class Auth2FactorRepositoryTest {

    private  File fileQr = new File("D:/Auth2Factor.png");
    private String key = "KOTIUZAYARWBVHRZ2OO73KSI77WXIYLE";

    @Before
    public void  beforeMethod(){
        System.out.println("@Before - genSecretKey");
    }

   // @Test
    public void genSecretKey()throws Exception {

        Auth2FactorRepository auth2FactorRepository =   Auth2FactorRepository.getInstance();
         key  = Auth2FactorRepository.getInstance().genSecretKey();
        Assert.assertNotNull(key);

        String urlAuth = auth2FactorRepository.genGoogleAuthenticatorBarCode(key,"auth2factor","krokropork-auth2factor");
        Assert.assertNotNull(urlAuth);


        auth2FactorRepository.createQRCode(urlAuth,fileQr.getAbsolutePath(),400,400);
        Assert.assertTrue(fileQr.exists());


        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        Assert.assertEquals(urlAuth,auth2FactorRepository.readQRCode(fileQr.getAbsolutePath(),hintMap));
        System.out.println("QRcode : " + auth2FactorRepository.readQRCode(fileQr.getAbsolutePath(),hintMap));


    }

    @Test
    public void genCheckCode()throws Exception {
        Auth2FactorRepository auth2FactorRepository =   Auth2FactorRepository.getInstance();
        String lastCode = null;
        String code = auth2FactorRepository.genOTPCode(key);
        Assert.assertNotNull(code);

        System.out.println(code);
    }



        @After
    public void afterMethod() {
        System.out.println("@After - genSecretKey");
    }

}