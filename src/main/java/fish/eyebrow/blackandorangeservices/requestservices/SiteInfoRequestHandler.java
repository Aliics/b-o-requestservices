package fish.eyebrow.blackandorangeservices.requestservices;

import fish.eyebrow.blackandorangeservices.requestservices.util.DatabaseUtils;
import org.json.JSONWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

@Path("site-info")
public class SiteInfoRequestHandler {
	static {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("about")
	@Produces("application/json")
	public static Response generateAbout() {
		final StringBuilder jsonResponse = new StringBuilder();
		final JSONWriter jsonResponseWriter = new JSONWriter(jsonResponse);

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			final String queryForAboutParagraphs = "SELECT content FROM about.about_paragraphs ORDER BY id ASC;";

			connection = DriverManager.getConnection(
					DatabaseUtils.setURLFromPropertiesFile(
							"webapps/requestservices/WEB-INF/classes/mariadb-login.properties"
					)
			);
			statement = connection.createStatement();
			resultSet = statement.executeQuery(queryForAboutParagraphs);

			jsonResponseWriter.object();
			jsonResponseWriter.key("aboutParagraphs");
			jsonResponseWriter.array();
			while (resultSet.next()) {
				jsonResponseWriter.value(resultSet.getString("content"));
			}
			jsonResponseWriter.endArray();
			jsonResponseWriter.endObject();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			DatabaseUtils.closeQuietly(resultSet);
			DatabaseUtils.closeQuietly(statement);
			DatabaseUtils.closeQuietly(connection);
		}

		return Response.status(Response.Status.OK)
				.header("Access-Control-Allow-Origin", "*")
				.entity(jsonResponse.toString())
				.build();
	}

	@GET
	@Path("menu")
	@Produces("application/json")
	public static Response generateMenu() {
		final StringBuilder jsonResponse = new StringBuilder();
		final JSONWriter jsonResponseWriter = new JSONWriter(jsonResponse);

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		final HashMap<Integer, String> menuGroupsMap = new HashMap<>();
		final ArrayList<ArrayList<String>> menuItemsList = new ArrayList<>();

		try {
			connection = DriverManager.getConnection(
					DatabaseUtils.setURLFromPropertiesFile(
							"webapps/requestservices/WEB-INF/classes/mariadb-login.properties"
					)
			);
			statement = connection.createStatement();

			final String queryForMenuGroups = "SELECT id, name FROM menu.menu_groups ORDER BY id ASC;";
			final StringBuilder queryForMenuItems = new StringBuilder()
					.append("SELECT group_id, name, money, description FROM menu.menu_items WHERE ");

			resultSet = statement.executeQuery(queryForMenuGroups);

			while (resultSet.next()) {
				menuGroupsMap.put(resultSet.getInt("id"), resultSet.getString("name"));
			}

			for (int a = 0; a < menuGroupsMap.size(); a++)
				queryForMenuItems.append("group_id = ").append(menuGroupsMap.keySet().toArray()[a])
						.append(a < menuGroupsMap.size() - 1 ? " OR " : " ORDER BY group_id ASC;");

			resultSet = statement.executeQuery(queryForMenuItems.toString());

			while (resultSet.next()) {
				final ArrayList<String> menuItem = new ArrayList<>();
				menuItem.add(resultSet.getString("group_id"));
				menuItem.add(resultSet.getString("name"));
				menuItem.add(resultSet.getString("money"));
				menuItem.add(resultSet.getString("description"));

				menuItemsList.add(menuItem);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			DatabaseUtils.closeQuietly(resultSet);
			DatabaseUtils.closeQuietly(statement);
			DatabaseUtils.closeQuietly(connection);
		}

		jsonResponseWriter.object();
		for (int a = 0; a < menuGroupsMap.size(); a++) {
			final int groupId = (int) menuGroupsMap.keySet().toArray()[a];
			jsonResponseWriter.key(menuGroupsMap.get(groupId));

			jsonResponseWriter.array();
			for (final ArrayList<String> itemList : menuItemsList) {
				final int itemGroupId = Integer.parseInt(itemList.get(0));
				if (groupId != itemGroupId)
					continue;

				jsonResponseWriter.object();
				jsonResponseWriter.key("name").value(itemList.get(1));
				jsonResponseWriter.key("money").value(itemList.get(2));
				jsonResponseWriter.key("description").value(itemList.get(3));
				jsonResponseWriter.endObject();
			}
			jsonResponseWriter.endArray();
		}
		jsonResponseWriter.endObject();

		return Response.status(Response.Status.OK)
				.header("Access-Control-Allow-Origin", "*")
				.entity(jsonResponse.toString())
				.build();
	}

	@GET
	@Path("contact")
	@Produces("application/json")
	public static Response generateContact() {
		final StringBuilder jsonResponse = new StringBuilder();
		final JSONWriter jsonResponseWriter = new JSONWriter(jsonResponse);

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			final String queryContactInformation = "SELECT * FROM contact.contact_info ORDER BY id ASC LIMIT 1;";

			connection = DriverManager.getConnection(
					DatabaseUtils.setURLFromPropertiesFile(
							"webapps/requestservices/WEB-INF/classes/mariadb-login.properties"
					)
			);

			statement = connection.createStatement();
			resultSet = statement.executeQuery(queryContactInformation);

			jsonResponseWriter.object();
			while (resultSet.next()) {
				jsonResponseWriter.key("address").value(resultSet.getString("address"));
				jsonResponseWriter.key("phoneNumber").value(resultSet.getString("phone_number"));
				jsonResponseWriter.key("emailAddress").value(resultSet.getString("email_address"));
				jsonResponseWriter.key("prefContactHours")
						.value(resultSet.getString("pref_contact_hours"));
			}

			jsonResponseWriter.endObject();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtils.closeQuietly(resultSet);
			DatabaseUtils.closeQuietly(statement);
			DatabaseUtils.closeQuietly(connection);
		}

		return Response.status(Response.Status.OK)
				.header("Access-Control-Allow-Origin", "*")
				.entity(jsonResponse.toString())
				.build();
	}
}
