package com.solaris.androidstone.utils;

/**
 * Created by hanxirui on 2016/12/24.
 */

public class Config {
    public enum Env {
        PROD("example.com"),
        DEV("dev.example.com");

        public final String host;
        Env(String host) {
            this.host = host;
        }
    }

    // API 要使用的 API 版本
    public final static int VERSION = 1;

    // 要去連的環境，這邊設定為正式環境
    public final static Env env = Env.PROD;

    public static String getEndPoint() {
        return getEndPoint(Config.env, VERSION);
    }

    public static String getEndPoint(Env env, int version) {
        return String.format("https://%s/api/v%d", env.host, version);
    }
}
