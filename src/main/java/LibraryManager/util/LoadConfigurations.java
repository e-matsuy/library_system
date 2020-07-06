package LibraryManager.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * configファイルからトークン生成用シークレットやDB設定値を読み込む
 */
public class LoadConfigurations {
    private static final String CONFIG_FILE_NAME = "config";
    private static final Properties properties;

    private LoadConfigurations(){
    }

    static {
        /*
            力技WEB-INFのパス取得取得
            1. ダミーでこのクラスのインスタンスを作る
            2. 作ったダミーからClassLoaderを経由してWEB-INF/Classesに作られるディレクトリの実際のパスを取得
            3. その親の親がWEB-INFなのでそのパスにCONFIG_FILE_NAMEをつけて設定ファイルまでのパスが完成
         */
        LoadConfigurations forGetRealPath = new LoadConfigurations();
        URL resourceUrl = forGetRealPath.getClass().getClassLoader().getResource("LibraryManager");
        String path = resourceUrl.toString().replace("file:","")+"../../" + CONFIG_FILE_NAME;

        properties = new Properties();
        try {
            try (BufferedReader configfile = Files.newBufferedReader(Paths.get(path))){
                properties.load(configfile);
            }
        }catch (IOException e){
            System.err.println("failed to load configure file.");
        }
    }

    public static String get(String key){
        // keyだけでも良いが、キーがない時のためにdefaultValueを設定しておく。
        //@TODO 確認。対象のキーが無い時はExceptionか""(空を返すか)
        return properties.getProperty(key, "");
    }
}
