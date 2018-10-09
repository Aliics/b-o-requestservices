package fish.eyebrow.blackandorangeservices.requestservices;

import fish.eyebrow.blackandorangeservices.requestservices.util.DatabaseUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

@Path("menu")
public class MenuRequestHandler {
	static {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@GET
	@Produces("application/json")
	public static Response generateMenu() {
		final StringBuilder jsonResponse = new StringBuilder();

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

			final String queryForMenuGroups = "SELECT id, name FROM menu_groups ORDER BY id ASC;";
			final StringBuilder queryForMenuItems = new StringBuilder()
					.append("SELECT group_id, name, description FROM menu_items WHERE ");

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

		jsonResponse.append("{");
		for (int a = 0; a < menuGroupsMap.size(); a++) {
			final int id = (int) menuGroupsMap.keySet().toArray()[a];
			final String name = menuGroupsMap.get(id);

			jsonResponse.append("\"").append(name).append("\"").append(": [");
			for (int b = 0; b < menuItemsList.size(); b++) {
				final ArrayList<String> menuItem = menuItemsList.get(b);
				final int groupId = Integer.valueOf(menuItem.get(0));
				final ArrayList<String> list = new ArrayList<>();
				list.add(menuItem.get(1));
				list.add(menuItem.get(2));

				if (groupId == id) {
					jsonResponse.append("{");
					for (int c = 0; c < list.size(); c++) {
						final String key = c == 0 ? "name" : "description"; // sadly hard coded for now

						jsonResponse.append("\"").append(key).append("\": \"").append(list.get(c)).append("\"")
								.append(c < list.size() - 1 ? ", " : "");
					}
					jsonResponse.append("}");

					if (b < menuItemsList.size() - 1)
						jsonResponse.append(groupId == Integer.valueOf(menuItemsList.get(b + 1).get(0)) ? ", " : "");
				}
			}
			jsonResponse.append("]");

			jsonResponse.append(a < menuGroupsMap.size() - 1 ? ", " : "");
		}

		jsonResponse.append("}");

		return Response.status(Response.Status.OK).entity(jsonResponse.toString()).build();
	}
}
