package com.example.chat.config.db.datasource;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ReadReplicaAspect {

	@Before("@annotation(ReadReplica)")
	public void setReadReplicaDataSource() {
		DataSourceContextHolder.useReplica();
	}

	@After("@annotation(ReadReplica)")
	public void clearDataSource() {
		DataSourceContextHolder.clear();
	}

}