package pl.za.xvacuum.guilds.mysql;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

public class ConnectionSource {
	
	private static ConnectionSource inst;
	public MysqlConnectionPoolDataSource dataSource;

	public static ConnectionSource getInstance() {
		return inst;
	}

	public ConnectionSource(String ip, String username, String password, String database, int port) {
		inst = this;
		dataSource = new MysqlConnectionPoolDataSource();

		try {
			startRegistry();
			dataSource.setUser(username);
			dataSource.setPassword(password);
			dataSource.setServerName(ip);
			dataSource.setPort(port);
			dataSource.setDatabaseName(database);
			InitialContext context = createContext();
			context.rebind("HrDS", dataSource);

		} catch (RemoteException | NamingException e) {
			e.printStackTrace();
		}
	}

	private void startRegistry() throws RemoteException {
		try {
			LocateRegistry.createRegistry(1099);
		} catch (ExportException e) {

		}
	}
	private InitialContext createContext() throws NamingException {
		InitialContext context;
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
		env.put(Context.PROVIDER_URL, "rmi://localhost:1099"); 
		context = new InitialContext(env);
		return context;
	}


}
