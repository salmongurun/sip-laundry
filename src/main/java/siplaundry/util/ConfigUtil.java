package siplaundry.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    private static Properties config;

    static{
        try {
            Properties properti = new Properties();
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("app.properties");
            properti.load(inputStream);

            config = properti;

        }  catch (IOException e) {
            throw new Error(e.getMessage());
        }
    }

    public static String get(String key) {
        return config.getProperty(key);
    }

}
