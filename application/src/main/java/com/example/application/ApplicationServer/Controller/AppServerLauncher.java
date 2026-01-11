package com.example.application.ApplicationServer.Controller;

import org.springframework.boot.SpringApplication;

import com.example.application.Application;

/**
 * アプリケーションサーバ専用の起動エントリ。
 */
public class AppServerLauncher {

    public static void main(String[] args) {
        // 環境変数 / JVM 引数で指定がなければデフォルトプロファイルを付与
        if (System.getenv("SPRING_PROFILES_ACTIVE") == null
                && System.getProperty("spring.profiles.active") == null) {
            System.setProperty("spring.profiles.active", "app-server");
        }

        SpringApplication.run(Application.class, args);
    }
}
