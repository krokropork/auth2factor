package th.co.nu.auth2factor.repository;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 6/6/2560.
 */

public class Auth2FactorRepositoryTest {


    @Before
    public void  beforeMethod(){
        System.out.println("@Before - genSecretKey");
    }

    @Test
    public void genSecretKey()throws Exception {
        Auth2FactorRepository auth2FactorRepository =   Auth2FactorRepository.getInstance();
        String key  = Auth2FactorRepository.getInstance().genSecretKey();


        auth2FactorRepository.createQRCode(
                auth2FactorRepository.genGoogleAuthenticatorBarCode(
                        auth2FactorRepository.genSecretKey(),"auth2factor","https://github.com/krokropork/auth2factor.git"
                )
                ,"D:/a.png",400,400);

        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        System.out.println("QRcode : " + auth2FactorRepository.readQRCode("D:/a.png",hintMap));

        String lastCode = null;
        while (true) {
            String code = auth2FactorRepository.genOTPCode(key);
            if (!code.equals(lastCode)) {
                // output a new 6 digit code
                System.out.println(code);
            }
            lastCode = code;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {};
        }

    }


    @After
    public void afterMethod() {
        System.out.println("@After - genSecretKey");
    }

}