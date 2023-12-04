package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropLoader {

    public static String getProp(String prop) throws IOException {
        Properties config = new Properties();
        System.out.println(new File("src/config.properties").getAbsolutePath());
        InputStream input = new FileInputStream(new File("src/config.properties"));
        config.load(input);
        return config.getProperty(prop);
    }
}
