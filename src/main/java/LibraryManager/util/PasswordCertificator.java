package LibraryManager.util;

import LibraryManager.datamodel.LoginRequest;
import LibraryManager.datamodel.User;
import LibraryManager.service.UserDataService;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import static java.util.Objects.isNull;

/**
 * パスワードを暗号化する。
 * やることてきには
 * パスワードの文字列をもらう + salt(入社日.getTimeを想定)を使って暗号化する
 * ここまで完成されていたら自分でやる気失せませんか。
 * https://www.casleyconsulting.co.jp/blog/engineer/153/
 */
public final class PasswordCertificator {
    /** パスワードを安全にするためのアルゴリズム (Java8以降のみ実装)*/
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    /** ストレッチング回数 */
    private static final int ITERATION_COUNT = 10000;
    /** 生成される鍵の長さ (HMAC-SHA-256に揃えてるらしい)*/
    private static final int KEY_LENGTH = 256;

    /**
     * 平文のパスワードとソルトから安全なパスワードを生成し、返却します
     *
     * @param password 平文のパスワード
     * @param salt ソルト
     * @return 安全なパスワード
     */
    public static String passwordEncryption(String password, String salt) {

        // パスワード→char[] salt→ sha256→ハッシュしてbyte[]
        char[] passCharAry = password.toCharArray();
        byte[] hashedSalt = getHashedSalt(salt);

        PBEKeySpec keySpec = new PBEKeySpec(passCharAry, hashedSalt, ITERATION_COUNT, KEY_LENGTH);

        SecretKeyFactory skf;
        try {
            skf = SecretKeyFactory.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        SecretKey secretKey;
        try {
            secretKey = skf.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        byte[] passByteAry = secretKey.getEncoded();

        // 生成されたバイト配列を16進数の文字列に変換
        StringBuilder sb = new StringBuilder(64);
        for (byte b : passByteAry) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    /**
     * ソルトをハッシュ化して返却します
     * ※ハッシュアルゴリズムはSHA-256を使用
     *
     * @param salt ソルト
     * @return ハッシュ化されたバイト配列のソルト
     */
    private static byte[] getHashedSalt(String salt) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        messageDigest.update(salt.getBytes());
        return messageDigest.digest();
    }

    /**
     * 従業員番号とパスワードで認証を行う
     * @param request ログインリクエスト情報を持ったオブジェクト
     * @return 認証結果
     */
    public static boolean passwordCertificate(LoginRequest request, User user){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        UserDataService userDataService = new UserDataService();
        String storedPassword = user.password();
        String hashedPassword = passwordEncryption(request.password(), simpleDateFormat.format(user.dateOfHire()));
        return storedPassword.equals(hashedPassword);
    }
}
