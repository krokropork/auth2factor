package th.co.nu.auth2factor.repository;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import th.co.nu.auth2factor.util.Rfc6238;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Map;

/**
 * Created by user on 6/6/2560.
 */
public class Auth2FactorRepository {

    private static Auth2FactorRepository ourInstance = new Auth2FactorRepository();

    public static Auth2FactorRepository getInstance() {
        return ourInstance;
    }

    private Auth2FactorRepository() {

    }

    public String encodeToString32(String s){
        Base32 base32 = new Base32();
        String secretKey = base32.encodeToString(s.getBytes());
        return secretKey;
    }

    public String decodeToString32(String s){
        Base32 base32 = new Base32();
        String secretKey = new String(base32.decode(s.getBytes()));
        return secretKey;
    }


    public String genSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        String secretKey = base32.encodeToString(bytes);
        return secretKey.toLowerCase().replaceAll("(.{4})(?=.{4})", "$1 ");
    }

    public String genOTPCode(String secretKey) {
        String key = secretKey.replace(" ", "").toUpperCase();
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(key);
        String hexKey = Hex.encodeHexString(bytes);
        long time = (System.currentTimeMillis() / 1000) / 30;
        String hexTime = Long.toHexString(time);
        return Rfc6238.getInstance().generateTOTP(hexKey,hexTime,"6");
    }


    public String genGoogleAuthenticatorBarCode(String secretKey, String account, String issuer){
        String key = secretKey.replace(" ", "").toUpperCase();
        try {
            return "otpauth://totp/"
                    + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(key, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }


    public void createQRCode(String barCodeData, String filePath, int height, int width) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);
        try (
                FileOutputStream out = new FileOutputStream(filePath)) {MatrixToImageWriter.writeToStream(matrix, "png", out);
        }
    }

    public String readQRCode( String filePath,Map hintMap) throws WriterException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
        return qrCodeResult.getText();
    }
}
