package pl.za.xvacuum.guilds.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Query {
	
	private String sx;
	
	public Query(String sx) {
		this.sx = sx;
	}
	
	public String getQuery() {
		return sx;
	}
	
	public void executeNow() {
		try {
			Connection c = ConnectionSource.getInstance().dataSource.getConnection();
			Statement s = c.createStatement();
			s.executeUpdate(getQuery());
			s.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}