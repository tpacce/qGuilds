package pl.za.xvacuum.guilds.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;

import pl.za.xvacuum.guilds.Main;

public class QueryExecutor {
	
	public static void execute(final Query query) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),
			new Runnable() {
				public void run() {
					try {
						Connection c = ConnectionSource.getInstance().dataSource.getConnection();
						Statement s = c.createStatement();
						s.executeUpdate(query.getQuery());
						s.close();
						c.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
		});
	}

}

