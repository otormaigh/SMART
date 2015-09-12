package ie.teamchile.smartapp.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by user on 9/12/15.
 */
public class CheckCert {
    private static String cert = "";

    public CheckCert() {
    }

    public static boolean isCertInvalid(Context context) {
        String fingerprintMD5 = "";
        Signature[] sigs = new Signature[0];
        try {
            sigs = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES).signatures;

        for (Signature sig : sigs) {
            byte[] hexBytes = sig.toByteArray();
            MessageDigest digest = MessageDigest.getInstance("MD5");

            byte[] md5digest = new byte[0];

            if (digest != null) {
                md5digest = digest.digest(hexBytes);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < md5digest.length; ++i) {
                    sb.append((Integer.toHexString((md5digest[i] & 0xFF) | 0x100)).substring(1, 3));
                }
                fingerprintMD5 = sb.toString();
            }
        }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return !fingerprintMD5.equals(cert);
    }
}
