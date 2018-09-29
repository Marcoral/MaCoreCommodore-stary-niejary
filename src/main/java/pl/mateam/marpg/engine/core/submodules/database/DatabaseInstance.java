package pl.mateam.marpg.engine.core.submodules.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.submodules.database.CommodoreDatabase;
import pl.mateam.marpg.api.submodules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging.Logger;
import pl.mateam.marpg.api.superclasses.CommodoreRuntimeException;
import pl.mateam.marpg.api.superclasses.CommodoreSubmodule;
import pl.mateam.marpg.api.superclasses.LogLevel;
import pl.mateam.marpg.engine.ControlPanel;
import pl.mateam.marpg.engine.Initializer;
import pl.mateam.marpg.engine.core.internal.ConfigPath;
import pl.mateam.marpg.engine.core.internal.CoreUtils;
import pl.mateam.marpg.engine.core.submodules.CommodoreCoreSetupException;

public class DatabaseInstance implements CommodoreDatabase, CommodoreSubmodule {
	private HikariDataSource dataSource;
	
	@Override
	public void setup() throws RuntimeException {
		CommodoreUtilsMessenging messenger = Commodore.getUtils().getMessengingUtils();
		CommodoreConfigurationFile config = CoreUtils.getConfigurationFile(ConfigPath.ENVIRONMENT);
		if(!config.exists())
			throw new CommodoreCoreSetupException("Database setup config is missing!");

		YamlConfiguration configData = config.getData();
		ConfigurationSection dataSection = configData.getConfigurationSection("Database");
		if(dataSection == null)
			throw new CommodoreCoreSetupException("Database setup section is missing!");

		HikariConfig hikariConfig = new HikariConfig();
		setupCredentials(dataSection, hikariConfig);
		
		dataSection = dataSection.getConfigurationSection("Setup");
		if(dataSection != null) {
			setupDataSourceProperties(dataSection, hikariConfig);
		}
		
		dataSource = new HikariDataSource(hikariConfig);
		
		
		try(Connection connection = dataSource.getConnection()) {	//Check for connection
			messenger.craftLogger(Logger.LOGGER_SETUP, LogLevel.FULL).colorSuccessHighlighted("Successfully connected to the database.").send();
		} catch (SQLException e) {
			throw new CommodoreCoreSetupException("Given credentials are invalid or database is down.");
		}
	}
	
	private void setupCredentials(ConfigurationSection dataSection, HikariConfig hikariConfig) {
		ConfigurationSection credentials = dataSection.getConfigurationSection("Connection");
		if(credentials == null)
			throw new CommodoreCoreSetupException("No \"Connection\" section inside \"Database\" section!");

		String url, username, password;
		url = "jdbc:mysql://";
		url += credentials.getString("IP", "localhost");
		url += "/" + credentials.getString("Database");
		username = credentials.getString("Username");
		password = credentials.getString("Password");
		
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
	}
	
	private void setupDataSourceProperties(ConfigurationSection dataSection, HikariConfig hikariConfig) {
		CommodoreUtilsMessenging messenger = Commodore.getUtils().getMessengingUtils();
		ConfigurationSection dataSourceProperties = dataSection.getConfigurationSection("DataSourceProperties");
		if(dataSourceProperties == null)
			return;
		messenger.craftLogger(Logger.LOGGER_SETUP, LogLevel.DEBUG).colorCasualHighlighted("DataSourceProperties have been found!").send();;
		for(String key : dataSourceProperties.getKeys(false)) {
			Object value = dataSourceProperties.get(key);
			hikariConfig.addDataSourceProperty(key, value);
			messenger.logCasualWithHighlight(key, " have been set to ", value.toString(), Logger.LOGGER_SETUP, LogLevel.DEBUG);
		};
	}
	
	@Override
	public void shutdown() {
		try {
			dataSource.close();
			Commodore.getUtils().getMessengingUtils().craftLogger(Logger.LOGGER_SETUP, LogLevel.FULL).colorCasualHighlighted("Connection with database have been closed.").send();
		} catch (Exception e) {
			ControlPanel.exceptionThrown(e);
		}
	}
	
	/* ------------------- */
	/* Implementation part */
	/* ------------------- */
	
	@Override
	public Connection getConnection() {
		if(Bukkit.isPrimaryThread())
			throw new CommodoreRuntimeException("This method must NOT be called in game thread!");
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
			return null;
		}
	}
	
	@Override
	public void performAction(Consumer<Connection> action) {
		if(Bukkit.isPrimaryThread())
			Bukkit.getScheduler().runTaskAsynchronously(Initializer.getInstance(), () -> performActionInCurrentThread(action));
		else
			performActionInCurrentThread(action);
	}
	
	@Override
	public void executeStatement(String preparedStatementCreationSQL, Object... preparedStatementArguments) {
		if(Bukkit.isPrimaryThread())
			Bukkit.getScheduler().runTaskAsynchronously(Initializer.getInstance(), () -> executeStatementInCurrentThread(preparedStatementCreationSQL, preparedStatementArguments));
		else
			executeStatementInCurrentThread(preparedStatementCreationSQL, preparedStatementArguments);		
	}
	
	@Override
	public void performActionOnData(Consumer<ResultSet> action, String preparedStatementCreationSQL, Object... preparedStatementArguments) {
		if(Bukkit.isPrimaryThread())
			Bukkit.getScheduler().runTaskAsynchronously(Initializer.getInstance(), () -> 
				performActionOnDataInCurrentThread(action, preparedStatementCreationSQL, preparedStatementArguments));
		else
			performActionOnDataInCurrentThread(action, preparedStatementCreationSQL, preparedStatementArguments);
		
	}
	
	@Override
	public void performActionOnUpdateResult(Consumer<Integer> action, String preparedStatementCreationSQL, Object... preparedStatementArguments) {
		if(Bukkit.isPrimaryThread())
			Bukkit.getScheduler().runTaskAsynchronously(Initializer.getInstance(), () -> 
				performActionOnUpdateResultInCurrentThread(action, false, preparedStatementCreationSQL, preparedStatementArguments));
		else
			performActionOnUpdateResultInCurrentThread(action, false, preparedStatementCreationSQL, preparedStatementArguments);		
	}

	@Override
	public void performThreadSafeActionOnUpdateResult(Consumer<Integer> action, String preparedStatementCreationSQL, Object... preparedStatementArguments) {
		if(Bukkit.isPrimaryThread())
			Bukkit.getScheduler().runTaskAsynchronously(Initializer.getInstance(), () -> 
				performActionOnUpdateResultInCurrentThread(action, true, preparedStatementCreationSQL, preparedStatementArguments));
		else
			performActionOnUpdateResultInCurrentThread(action, true, preparedStatementCreationSQL, preparedStatementArguments);		
	}

	private void performActionInCurrentThread(Consumer<Connection> action) {
		try(Connection connection = dataSource.getConnection()) {
			action.accept(connection);
		} catch (SQLException e) {
			Commodore.getUtils().getMessengingUtils().logError("Error while obtaining Connection object! Is database down?", Logger.LOGGER_RUNTIME, LogLevel.ERROR);
			ControlPanel.exceptionThrown(e);
		}
	}

	private void executeStatementInCurrentThread(String preparedStatementCreationSQL, Object... preparedStatementArguments) {
		try(Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = CoreUtils.prepareStatementInline(connection, preparedStatementCreationSQL, preparedStatementArguments)) {
			preparedStatement.executeQuery();
		} catch (SQLException e) {
			Commodore.getUtils().getMessengingUtils().logError("Error while obtaining PreparedStatement object! Is database down?", Logger.LOGGER_RUNTIME, LogLevel.ERROR);
			ControlPanel.exceptionThrown(e);
		}
	}
	
	private void performActionOnDataInCurrentThread(Consumer<ResultSet> action, String preparedStatementCreationSQL, Object... preparedStatementArguments) {
		try(Connection connection = dataSource.getConnection(); 
				PreparedStatement preparedStatement = CoreUtils.prepareStatementInline(connection, preparedStatementCreationSQL, preparedStatementArguments)) {
			ResultSet resultSet = preparedStatement.executeQuery();
			performRequestedActionOnResultSet(action, resultSet);
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
	}
	
	private void performRequestedActionOnResultSet(Consumer<ResultSet> action, ResultSet resultSet) {
		action.accept(resultSet);
		try {
			resultSet.close();
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
	}
	
	private void performActionOnUpdateResultInCurrentThread(Consumer<Integer> action, boolean threadSafe, String preparedStatementCreationSQL, Object... preparedStatementArguments) {
		try(Connection connection = dataSource.getConnection(); 
				PreparedStatement preparedStatement = CoreUtils.prepareStatementInline(connection, preparedStatementCreationSQL, preparedStatementArguments)) {
			int result = preparedStatement.executeUpdate();
			if(threadSafe)
				Bukkit.getScheduler().runTask(Initializer.getInstance(), () -> action.accept(result));
			else
				action.accept(result);
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
	}
}
