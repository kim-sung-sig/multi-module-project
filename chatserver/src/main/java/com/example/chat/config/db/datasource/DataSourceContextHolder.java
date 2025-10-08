package com.example.chat.config.db.datasource;

public class DataSourceContextHolder {

	private static final ThreadLocal<DataSourceType> CONTEXT = new ThreadLocal<>();

	public static void useSource() { CONTEXT.set(DataSourceType.SOURCE); }

	public static void useReplica() { CONTEXT.set(DataSourceType.REPLICA); }

	public static DataSourceType get() { return CONTEXT.get(); }

	public static void clear() { CONTEXT.remove(); }

}