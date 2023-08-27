package com.example.apijsondemo;


import apijson.Log;
import apijson.framework.APIJSONApplication;
import apijson.framework.APIJSONCreator;
import apijson.framework.APIJSONSQLConfig;
import apijson.orm.SQLConfig;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

public class APIJSONInit {

    public static void init(APIJSONSQLConfigInit configInit) {
        Log.DEBUG = true;
        try {
            var shutdownWhenServerError = false;
            var creator = new APIJSONCreator<Long>() {
                @Override
                public SQLConfig createSQLConfig() {
                    return configInit.getSqlConfig();
                }
            };
            APIJSONApplication.init(shutdownWhenServerError, creator);
        } catch (Throwable err) {
            System.out.println("APIJSON ERROR! " + err.getMessage());
        }
    }

    public static class APIJSONSQLConfigInit extends APIJSONSQLConfig {

        @Getter
        private final SqlConfig sqlConfig;

        public APIJSONSQLConfigInit(APIJSONDBConfig apiJsonDatasourceConfig) {
            this.sqlConfig = new SqlConfig(apiJsonDatasourceConfig);
        }

        public static class SqlConfig extends APIJSONSQLConfig {

            static {
                DEFAULT_DATABASE = DATABASE_MYSQL;
                DEFAULT_SCHEMA = "apijsondemo";

                TABLE_KEY_MAP.put("User", "apijson_user");
                TABLE_KEY_MAP.put("Privacy", "apijson_privacy");
            }

            private final APIJSONDBConfig apiJsonDatasourceConfig;

            public SqlConfig(APIJSONDBConfig apiJsonDatasourceConfig) {
                this.apiJsonDatasourceConfig = apiJsonDatasourceConfig;
            }

            @Override
            public String getDBVersion() {
                return apiJsonDatasourceConfig.getMysqlVersion();
            }

            @JSONField(serialize = false)
            @Override
            public String getDBUri() {
                return apiJsonDatasourceConfig.getUrl();
            }

            @JSONField(serialize = false)
            @Override
            public String getDBAccount() {
                return apiJsonDatasourceConfig.getUsername();
            }

            @JSONField(serialize = false)
            @Override
            public String getDBPassword() {
                return apiJsonDatasourceConfig.getPassword();
            }

        }

    }

    @Getter
    @Configuration(value = "apiJsonDbConfig")
    public static class APIJSONDBConfig {

        /**
         * jdbc:mysql://host:3306/db?useUnicode=true&characterEncoding=UTF-8&useSSL=false
         */
        @Value("${spring.datasource.url}")
        private String url;

        @Value("${spring.datasource.username}")
        private String username;

        @Value("${spring.datasource.password}")
        private String password;

        /**
         * 5.7.22
         */
        @Value("${mysql.version}")
        private String mysqlVersion;

    }

}
