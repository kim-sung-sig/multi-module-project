package com.example.chat.config.db.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		DataSourceType current = DataSourceContextHolder.get();
		if (current != null) {
			log.debug("Routing to {} dataSource by DataSourceContextHolder", current.getKey());
			return current.getKey();
		}

		boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
		DataSourceType lookupType = readOnly
				? DataSourceType.REPLICA
				: DataSourceType.SOURCE;

		log.debug("Routing to {} dataSource", lookupType.getKey());
		return lookupType.getKey();
	}
}