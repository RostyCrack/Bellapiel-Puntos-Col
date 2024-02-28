package com.hardtech.bellapielpuntoscol.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.hardtech.bellapielpuntoscol.context.datasource.DataSourceContextHolder;
import com.hardtech.bellapielpuntoscol.context.datasource.DataSourceEnum;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

@Component
public class DataSourceRouting extends AbstractRoutingDataSource {
	private final ICGFrontDataSourceConfig icgFrontDataSourceConfig;
	private final DataSourceContextHolder dataSourceContextHolder;

	public DataSourceRouting(DataSourceContextHolder dataSourceContextHolder, ICGFrontDataSourceConfig dataSourceOneConfig) {
		this.icgFrontDataSourceConfig = dataSourceOneConfig;
		this.dataSourceContextHolder = dataSourceContextHolder;

		Map<Object, Object> dataSourceMap = new HashMap<>();
		dataSourceMap.put(DataSourceEnum.ICGFRONT, IcgFrontDataSource());
		dataSourceMap.put(DataSourceEnum.MNG_BP, MngTwoDataSource());
		this.setTargetDataSources(dataSourceMap);
		this.setDefaultTargetDataSource(IcgFrontDataSource());
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return dataSourceContextHolder.getBranchContext();
	}

	public DataSource IcgFrontDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(icgFrontDataSourceConfig.getUrl());
		dataSource.setUsername(icgFrontDataSourceConfig.getUsername());
		dataSource.setPassword(icgFrontDataSourceConfig.getPassword());
		return dataSource;
	}

	public DataSource MngTwoDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl("jdbc:sqlserver://192.168.2.6;database=MNG_BP");
		dataSource.setUsername("ICGADMIN");
		dataSource.setPassword("BP*.S@3s3c4r5ty*.");
		return dataSource;
	}
}