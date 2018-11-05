package com.ch.sockettest.dao;
/**
 * 
 * 测试报告统计信息
 * 
 * @author fdw
 *
 */
public class TestReport {

	public String id;
	public String netType;
	public double upStreamRate;
	public double downStreamRate;
	public double totalStreamRate;
	public String testResult;
	public String testTime;
	
	
	public TestReport() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public double getUpStreamRate() {
		return upStreamRate;
	}
	public void setUpStreamRate(double upStreamRate) {
		this.upStreamRate = upStreamRate;
	}
	public double getDownStreamRate() {
		return downStreamRate;
	}
	public void setDownStreamRate(double downStreamRate) {
		this.downStreamRate = downStreamRate;
	}
	public double getTotalStreamRate() {
		return totalStreamRate;
	}
	public void setTotalStreamRate(double totalStreamRate) {
		this.totalStreamRate = totalStreamRate;
	}
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	public String getTestTime() {
		return testTime;
	}
	public void setTestTime(String testTime) {
		this.testTime = testTime;
	}
	@Override
	public String toString() {
		return "TestReport [id=" + id + ", netType=" + netType + ", upStreamRate=" + upStreamRate + ", downStreamRate="
				+ downStreamRate + ", totalStreamRate=" + totalStreamRate + ", testResult=" + testResult + ", testTime="
				+ testTime + "]";
	}
	
}
