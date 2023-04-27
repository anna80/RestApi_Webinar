package configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigurationReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationReader.class);

    private ConfigurationReader() {
    }

    public static Properties getPropertiesFromFile(String pathToFile) {
        Properties properties = new Properties();
        try (final InputStream stream = ConfigurationReader.class.getClassLoader().getResourceAsStream(pathToFile)) {
            properties.load(stream);
        } catch (IOException e) {
            LOGGER.error("file is not exists!!!");
        }
        return properties;
    }
}

