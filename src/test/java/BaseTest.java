import configuration.ConfigurationReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;

import java.util.Properties;

import static utils.RestClient.authorizeUser;

public class BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    protected Properties properties = ConfigurationReader.getPropertiesFromFile("base.properties");

    @BeforeMethod
    public void beforeTest(){
        LOGGER.info("{} properties were initialized", properties.size());
        authorizeUser();
    }


}
