package fish.eyebrow.blackandorangeservices.requestservices;

import fish.eyebrow.blackandorangeservices.requestservices.util.DatabaseUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.sql.*;
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
		final HashMap<Integer, HashMap<String, String>> menuItemsMap = new HashMap<>();

		try {
			connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/menu", "root", "");
			statement = connection.createStatement();

			final String queryForMenuGroups = "SELECT id, name FROM menu_groups;";
			final StringBuilder queryForMenuItems = new StringBuilder()
					.append("SELECT group_id, name, description FROM menu_items WHERE ");

			resultSet = statement.executeQuery(queryForMenuGroups);

			while (resultSet.next()) {
				menuGroupsMap.put(resultSet.getInt("id"), resultSet.getString("name"));
			}

			for (int a = 0; a < menuGroupsMap.size(); a++)
				queryForMenuItems.append("group_id = ").append(menuGroupsMap.keySet().toArray()[a])
						.append(a < menuGroupsMap.size() - 1 ? " OR " : ";");

			resultSet = statement.executeQuery(queryForMenuItems.toString());

			while (resultSet.next()) {
				final HashMap<String, String> menuItem = new HashMap<>();
				menuItem.put("name", resultSet.getString("name"));
				menuItem.put("description", resultSet.getString("description"));

				menuItemsMap.put(resultSet.getInt("group_id"), menuItem);
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
			for (int b = 0; b < menuItemsMap.size(); b++) {
				final int groupId = (int) menuItemsMap.keySet().toArray()[b];
				final HashMap<String, String> list = menuItemsMap.get(groupId);

				jsonResponse.append("{");
				if (groupId == id) {
					for (int c = 0; c < list.size(); c++) {
						final String key = (String) list.keySet().toArray()[c];

						jsonResponse.append("\"").append(key).append("\": \"").append(list.get(key)).append("\"")
								.append(c < list.size() - 1 ? "," : "");
					}
				}

				jsonResponse.append("}").append(a < menuGroupsMap.size() - 1 ? "," : "");
			}
			jsonResponse.append("]");
		}

		jsonResponse.append("}");

		return Response.status(Response.Status.OK).entity(jsonResponse.toString()).build();
	}
}
