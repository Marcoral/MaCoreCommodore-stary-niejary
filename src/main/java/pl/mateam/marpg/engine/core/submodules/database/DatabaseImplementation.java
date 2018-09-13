package pl.mateam.marpg.engine.core.submodules.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.modules.database.CommodoreDatabase;
import pl.mateam.marpg.api.modules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.api.modules.utils.CommodoreMessengingUtils.Logger;
import pl.mateam.marpg.api.superclasses.CommodoreSubmodule;
import pl.mateam.marpg.api.superclasses.LogLevel;
import pl.mateam.marpg.engine.ControlPanel;
import pl.mateam.marpg.engine.core.submodules.CommodoreCoreSetupException;
import pl.mateam.marpg.engine.core.submodules.ConfigPath;
import pl.mateam.marpg.engine.core.submodules.CoreLoadingUtils;

public class DatabaseImplementation implements CommodoreDatabase, CommodoreSubmodule {
	private String user;
	private String database;
	private String password;
	private String port;
	private String hostname;
	private Connection connection;

	/* ------------------- */
	/* Core submodule part */
	/* ------------------- */
	
	@Override
	public void setup() throws RuntimeException {
		CommodoreConfigurationFile config = CoreLoadingUtils.getConfigurationFile(ConfigPath.ENVIRONMENT);
		if(!config.exists())
			throw new CommodoreCoreSetupException("Database setup config is missing!");

		YamlConfiguration configData = config.getData();
		hostname = configData.getString("Connection.IP");
		port = configData.getString("Connection.Port");
		database = configData.getString("Connection.Database");
		user = configData.getString("Connection.Username");
		password = configData.getString("Connection.Password");
		
		if(getConnection() == null)
			throw new CommodoreCoreSetupException("Given credentials are invalid or database is down.");
		Commodore.getUtils().getMessengingUtils().logSuccessWithHighlight("", "Successfully connected to the database", "", Logger.LOGGER_SETUP, LogLevel.FULL);
	}
	
	@Override
	public void shutdown() {
		try {
			if(!connection.isClosed())
				connection.close();
			Commodore.getUtils().getMessengingUtils().logCasualWithHighlight("", "Connection with database have been closed", "", Logger.LOGGER_SETUP, LogLevel.FULL);
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
	}
	
	/* ------------------- */
	/* Implementation part */
	/* ------------------- */
	
	@Override
	public Connection getConnection() {
		try {
			if (connection != null && !connection.isClosed())
				return connection;
			
			String connectionURL = "jdbc:mysql://" + hostname + ":" + port;
			if (database != null)
				connectionURL = connectionURL + "/" + database;
			
			connection = DriverManager.getConnection(connectionURL, user, password);
		} catch(Exception e) {
			ControlPanel.exceptionThrown(e);
		}
		return connection;
	}
}
