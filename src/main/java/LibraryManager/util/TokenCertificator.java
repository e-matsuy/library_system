package LibraryManager.util;
import LibraryManager.datamodel.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

public final class TokenCertificator {

    static final Long TOKEN_EXPIRE_RANGE = 3600000l;
    static final String CLAIM_PARAMETER_ID = "id";
    final Algorithm signForToken;
    JWTVerifier verifier;

    /**
     * @param token_secret トークンの認証に使う鍵を生成するためのシークレット
     */
    public TokenCertificator(String token_secret){
        signForToken = Algorithm.HMAC256(token_secret);
        verifier = JWT.require(signForToken).build();
    }

    /**
     * 指定されたUsersのidからトークンを生成する
     * @param requestUser 認証に成功したユーザー
     * @return 新たに生成したトークン
     */
    public String generateToken(User requestUser){
        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + TOKEN_EXPIRE_RANGE);
        int userId = requestUser.id();

        String newGeneratedToken = JWT.create().withExpiresAt(expireTime).withClaim(CLAIM_PARAMETER_ID, userId).sign(signForToken);
        return newGeneratedToken;
    }

    /**
     * トークンの署名を検証する。
     * @param token 検証するトークン文字列
     * @return デコード済みトークン情報
     * @throws TokenExpiredException トークンが期限切れのとき
     * @throws SignatureVerificationException トークンの署名に異常があった時
     */
    private DecodedJWT verifyToken(String token) throws TokenExpiredException,SignatureVerificationException {
        DecodedJWT decodedToken = verifier.verify(token);
        return decodedToken;
    }

    /**
     * トークンから認証されたユーザーを取り出す。
     * @param token デコードするトークン
     * @return トークンを取得したユーザー
     * @throws TokenExpiredException トークンが期限切れのとき
     * @throws SignatureVerificationException トークンの署名に異常があった時
     */
    public User decodeToken(String token) throws TokenExpiredException,SignatureVerificationException{
        DecodedJWT decoded = verifyToken(token);
        int userId = decoded.getClaim(CLAIM_PARAMETER_ID).asInt();
        return null;
    }

}
