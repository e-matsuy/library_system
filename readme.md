# 図書管理システム
社内の貸出図書を管理するシステム

## 開発環境
* macOS 10.15.5
* IntelliJ IDEA 2020.1.2
* OpenJDK 12.0.2
* Docker version 19.03.8, build afacb8b
* docker-compose version 1.25.5, build 8a1c60f6
* Tomcat 9.0.36 by Homebrew

## 開発環境構築
1. リポジトリのクローン

    `git clone https://github.com/e-matsuy/library_system.git`
1. データベースの起動

    `cd cloned_dir/docker;docker-compose up -d`
1. IntelliJ IDEAの起動

    `cd ..; idea .`
1. Tomcatへのデプロイ設定
    1. Run/Debug configurationを開く<br>
    1. 左上+ボタンをおし、"Tomcat Server"のlocalを選ぶ
    1. application serverのComfigureを押して、TomcatのHome(libexec)のパスを入力してok
    1. 一番下、"before launch"の+を押して"Build Artifacts"をクリック。デフォルトだとwarが2つ出るのでexplodedじゃない方にチェクしてok
    1. apply
    1. Run して デフォルトブラウザが開いてWebページが表示されれば完了
