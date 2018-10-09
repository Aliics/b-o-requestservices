package fish.eyebrow.blackandorangeservices.requestservices.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseUtils {
	public static String setURLFromPropertiesFile(Properties properties) {
		final String jdbc = (String) properties.get("jdbc");
		final String host = (String) properties.get("host");
		final String port = (String) properties.get("port");
		final String database = (String) properties.get("database");
		final String user = (String) properties.get("user");
		final String password = (String) properties.get("password");

		return formatStringFromParams(jdbc, host, port, database, user, password);
	}

	public static String setURLFromPropertiesFile(String filePath) {
		final Properties properties = new Properties();
		try {
			properties.load(new BufferedInputStream(new FileInputStream(filePath)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return setURLFromPropertiesFile(properties);
	}

	public static String formatStringFromParams(String jdbc, String host, String port, String database, String user,
	                                            String password) {

		return "jdbc:" + jdbc +
				"://" + host +
				(port != null ? ":" + port : "") +
				"/" + (database == null ? "" : database) +
				"?user=" + user +
				(password != null ? "&password=" + password : "");
	}

	public static void closeQuietly(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeQuietly(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeQuietly(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
