package pl.mateam.marpg.engine.core.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.submodules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.api.submodules.text.TextNode;
import pl.mateam.marpg.engine.ControlPanel;
import pl.mateam.marpg.engine.Initializer;

public class CoreUtils {
	
	/*-------------------*/
	/*	Core setup utils */
	/*-------------------*/
	
	public static CommodoreConfigurationFile getConfigurationFile(ConfigPath configPath) {
		return Commodore.getFilesManager().getConfig(Initializer.getInstance().getName(), configPath.getPath());
	}
	
	/*------------*/
	/*	SQL utils */
	/*------------*/
	
	public static String buildTransaction(String... statements) {
		StringBuilder builder = new StringBuilder("START TRANSACTION;");
		for(String statement : statements) {
			builder.append(statement);
			builder.append("; ");
		}
		builder.append("COMMIT;");
		return builder.toString();
	}
	
	public static String field(String tableName, String fieldName) {
		return tableName + "." + fieldName;
	}
	
	public static String where(String statementWithoutCondition, String condition) {
		if(condition == null)
			return statementWithoutCondition;
		statementWithoutCondition = statementWithoutCondition.replaceAll(";", "");
		StringBuilder builder = new StringBuilder(statementWithoutCondition);
		builder.append(" WHERE ");
		builder.append(condition);
		builder.append(";");
		return builder.toString();
	}
	
	public static String leftJoin(String statement, String joinedTable, String fieldName) {
		return join(" LEFT JOIN ", statement, joinedTable, fieldName);
	}
	
	public static String rightJoin(String statement, String joinedTable, String fieldName) {
		return join(" RIGHT JOIN ", statement, joinedTable, fieldName);
	}
	
	public static String innerJoin(String statement, String joinedTable, String fieldName) {
		return join(" INNER JOIN ", statement, joinedTable, fieldName);
	}
	
	private static String join(String keyword, String statement, String joinedTable, String fieldName) {
		StringBuilder builder = new StringBuilder(statement.replaceAll(";", ""));
		builder.append(keyword);
		builder.append(joinedTable);
		builder.append(" USING(");
		builder.append(fieldName);
		builder.append(");");
		return builder.toString();
	}
	
	public static PreparedStatement prepareStatementInline(Connection connection, String statementBase, Object... args) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(statementBase);
		return prepareStatementInline(statement, args);
	}
	
	public static PreparedStatement prepareStatementInline(PreparedStatement statement, Object... args) {
		try {
			int index = 1;
			for(Object arg : args)
				statement.setObject(index++, arg);
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
		return statement;
	}
	
	public static void executeStatementInline(Connection connection, String statementBase, Object... args) {
		try {
			prepareStatementInline(connection, statementBase, args).execute();
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
	}
	
	/*-----------------------*/
	/*	Formatted text utils */
	/*-----------------------*/
	
	public static void addFormatArguments(List<Object> arguments, TextNode formatNode, Object... objects) {
		if(objects.length > 0 && objects[0] != null) {
			String pattern = Commodore.getTextManager().getNode(formatNode);
			arguments.add(MessageFormat.format(pattern, objects));
		} else
			arguments.add("");
	}
	
	/*----------------*/
	/*	Generic utils */
	/*----------------*/
	
	public static <T extends Comparable<T>> T minOrFirst(T firstObject, T secondObject) {
		return firstObject.compareTo(secondObject) > 0? secondObject : firstObject;		
	}
	
	public static <T extends Comparable<T>> T maxOrFirst(T firstObject, T secondObject) {
		return firstObject.compareTo(secondObject) < 0? secondObject : firstObject;		
	}
}
