package LibraryManager.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.Properties;

import static java.util.Objects.isNull;

/**
 * configファイルからトークン生成用シークレットやDB設定値を読み込む
 */
public class LoadConfigurations {
    private static final String JDBC_URL_CONFIG_KEYNAME = "jdbc_url";
    private static final String DATABASE_USER_NAME_CONFIG_KEYNAME = "user_name";
    private static final String DATABASE_PASSWORD_CONFIG_KEYNAME = "password";
    private static final String TOKEN_SECRET_CONFIG_KEYNAME = "secret";
    private static final String CONFIG_FILE_PATH_ENV_NAME = "config_file_path";
    private static final Properties properties;

    static {
        properties = new Properties();
        String configFilePath = getFilePath(CONFIG_FILE_PATH_ENV_NAME);
        try {
            try (BufferedReader configfile = Files.newBufferedReader(Paths.get(configFilePath))){
                properties.load(configfile);
            }
        }catch (IOException e){
            System.err.println("failed to load configure file.");
        }
    }

    public static String get(String key){
        // keyだけでも良いが、キーがない時のためにdefaultValueを設定しておく。
        return properties.getProperty(key, "");
    }

    public static String getJdbcUrl(){
        return get(JDBC_URL_CONFIG_KEYNAME);
    }

    public static String getDatabaseUserName(){
        return get(DATABASE_USER_NAME_CONFIG_KEYNAME);
    }

    public static String getDatabasePassword(){
        return get(DATABASE_PASSWORD_CONFIG_KEYNAME);
    }

    public static String getTokenSecret(){
        return get(TOKEN_SECRET_CONFIG_KEYNAME);
    }

    /**
     * 環境変数/システムプロパティに設定された接続情報などを取り出す。
     * システムプロパティと環境変数どちらにも値が登録されていたときは、環境変数の値を優先する。
     * @param key 取得したいプロパティのkeyの名前
     * @return そのプロパティの値
     * @throws InvalidParameterException 指定されたプロパティが環境変数とシステムプロパティどちらにも登録されていなかった時
     */
    public static String getFilePath(String key){
        String requested_value;
        requested_value = System.getenv(key);
        if(isNull(requested_value)){
            requested_value = System.getProperty(key, "");
        }
        if(requested_value.isEmpty()){
            throw new InvalidParameterException();
        }
        return requested_value;
    }
}
