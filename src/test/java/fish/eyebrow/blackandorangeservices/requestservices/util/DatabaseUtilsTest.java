package fish.eyebrow.blackandorangeservices.requestservices.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static fish.eyebrow.blackandorangeservices.requestservices.util.DatabaseUtils.formatStringFromParams;
import static fish.eyebrow.blackandorangeservices.requestservices.util.DatabaseUtils.setURLFromPropertiesFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseUtilsTest {

	private static Properties properties;

	@BeforeAll
	static void setupPropertiesForAssertion() throws IOException {
		properties = new Properties();
		properties.load(new BufferedInputStream(new FileInputStream("src/test/resources/mariadb-login.properties")));
	}

	@Test
	void checkIfPropertiesAreSet() {
		assertEquals(
				"jdbc:mariadb://localhost:3306/menu?user=root&password=dicks",
				setURLFromPropertiesFile(properties)
		);
	}

	@Test
	void checkIfWorksWithoutOptional() {
		assertEquals("jdbc:mysql://youtube.com/?user=google",
				formatStringFromParams(
						"mysql",
						"youtube.com",
						null,
						null,
						"google",
						null)
				);
	}
}