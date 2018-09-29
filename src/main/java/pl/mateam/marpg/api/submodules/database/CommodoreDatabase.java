package pl.mateam.marpg.api.submodules.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.function.Consumer;

import pl.mateam.marpg.api.CommodoreInformativeAnnotations.RunsAsync;
import pl.mateam.marpg.api.CommodoreInformativeAnnotations.UseAsyncOnly;

public interface CommodoreDatabase {
	 /* Be very cautious when using this method! It can not be used in main thread
	  * and should be called only if you have to return some value from some method.
	  * Remember then to close connection manually, when the job is done! */
	@UseAsyncOnly Connection getConnection();
	
	@RunsAsync void performAction(Consumer<Connection> action);
	@RunsAsync void executeStatement(String preparedStatementCreationSQL, Object... preparedStatementArguments);
	@RunsAsync void performActionOnData(Consumer<ResultSet> action, String preparedStatementCreationSQL, Object... preparedStatementArguments);
	@RunsAsync void performActionOnUpdateResult(Consumer<Integer> action, String preparedStatementCreationSQL, Object... preparedStatementArguments);
	@RunsAsync void performThreadSafeActionOnUpdateResult(Consumer<Integer> action, String preparedStatementCreationSQL, Object... preparedStatementArguments);
}