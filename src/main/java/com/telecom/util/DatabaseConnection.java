package com.telecom.util;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {

    private static final Logger LOGGER =
            Logger.getLogger(DatabaseConnection.class.getName());

    private static volatile Map<String, String> dotEnvMap = new HashMap<>();
    private static volatile long lastDotEnvModified = -1L;

    private static Map<String, String> getDotEnv() {
        File[] candidates = new File[] {
                new File(".env"),
                new File("../.env"),
                new File(System.getProperty("user.dir"), ".env")
        };

        File active = null;
        for (File file : candidates) {
            if (file.exists() && file.isFile()) {
                active = file;
                break;
            }
        }

        if (active == null) {
            return dotEnvMap;
        }

        long modified = active.lastModified();
        if (modified == lastDotEnvModified && !dotEnvMap.isEmpty()) {
            return dotEnvMap;
        }

        Map<String, String> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(active, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int eqIdx = line.indexOf('=');
                if (eqIdx > 0) {
                    String key = line.substring(0, eqIdx).trim();
                    String val = line.substring(eqIdx + 1).trim();
                    if ((val.startsWith("\"") && val.endsWith("\"")) ||
                        (val.startsWith("'") && val.endsWith("'"))) {
                        val = val.substring(1, val.length() - 1);
                    }
                    map.put(key, val);
                }
            }
            lastDotEnvModified = modified;
            dotEnvMap = map;
            LOGGER.info("Loaded environment configuration from " + active.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "Could not load .env file from " + active.getAbsolutePath(), e);
        }
        return dotEnvMap;
    }

    public static String getSetting(String key, String defaultValue) {
        String env = System.getenv(key);
        if (env != null && !env.isBlank()) {
            return env.trim();
        }
        String prop = System.getProperty(key);
        if (prop != null && !prop.isBlank()) {
            return prop.trim();
        }
        String dotenv = getDotEnv().get(key);
        if (dotenv != null && !dotenv.isBlank()) {
            return dotenv.trim();
        }
        return defaultValue;
    }

    public static String getSetting(String key) {
        return getSetting(key, null);
    }

    public static String getHost() {
        return getSetting("DB_HOST", "localhost");
    }

    public static String getPort() {
        return getSetting("DB_PORT", "3306");
    }

    public static String getDbName() {
        return getSetting("DB_NAME", "telecom_optimization");
    }

    public static String getUser() {
        return getSetting("DB_USER", "telecom_app");
    }

    public static String getPassword() {
        return getSetting("DB_PASSWORD");
    }

    public static String getUrl() {
        String explicitUrl = getSetting("DB_URL");
        if (explicitUrl != null && !explicitUrl.isBlank()) {
            return explicitUrl.trim();
        }

        String host = getHost();
        String port = getPort();
        String dbName = getDbName();
        String sslMode = getSetting("DB_SSL_MODE");

        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://").append(host).append(":").append(port).append("/").append(dbName);

        // Smart SSL and timezone parameters
        if ("REQUIRED".equalsIgnoreCase(sslMode) || host.contains("aivencloud.com")) {
            sb.append("?sslMode=REQUIRED&serverTimezone=UTC");
        } else if ("DISABLED".equalsIgnoreCase(sslMode) || "false".equalsIgnoreCase(sslMode) ||
                   "localhost".equalsIgnoreCase(host) || "127.0.0.1".equals(host)) {
            sb.append("?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        } else {
            sb.append("?serverTimezone=UTC&allowPublicKeyRetrieval=true");
        }

        return sb.toString();
    }

    static {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    try {
                        AbandonedConnectionCleanupThread.checkedShutdown();
                        System.out.println("MySQL cleanup completed.");
                    } catch (Exception e) {
                        System.out.println("MySQL cleanup error.");
                        e.printStackTrace();
                    }
                })
        );
    }

    public static Connection getConnection() {
        String url = getUrl();
        String user = getUser();
        String password = getPassword();

        if (password == null || password.isBlank()) {
            String errorMsg = "DB_PASSWORD environment variable is not configured.";
            System.err.println(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                    DriverManager.getConnection(url, user, password);

            System.out.println("Database connected successfully to "
                    + sanitizeUrl(url) + " (user: " + user + ")");

            return connection;

        } catch (Exception e) {
            Throwable root = getRootCause(e);
            String safeMessage = "Database connection failed! Root cause: "
                    + root.getClass().getSimpleName() + ": " + root.getMessage();
            System.err.println(safeMessage);
            LOGGER.log(Level.SEVERE, safeMessage, e);
            throw new RuntimeException("Database connection failed: " + root.getMessage(), e);
        }
    }

    public static Throwable getRootCause(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root;
    }

    public static String sanitizeUrl(String url) {
        if (url == null) return "";
        return url.replaceAll("://[^:@/]+:[^@/]+@", "://***:***@");
    }
}
