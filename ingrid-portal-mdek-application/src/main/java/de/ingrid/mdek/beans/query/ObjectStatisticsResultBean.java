package de.ingrid.mdek.beans.query;

import java.util.Map;

public class ObjectStatisticsResultBean {

	// Map object types (ObjectType) to statistics
	private Map<Integer, StatisticsBean> resultMap;

	public Map<Integer, StatisticsBean> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<Integer, StatisticsBean> resultMap) {
		this.resultMap = resultMap;
	}
}
