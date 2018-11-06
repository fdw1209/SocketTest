package com.ch.sockettest.dao;
/**
 * wifi吞吐量数据信息
 * 
 * @author fdw
 *
 */
public class WifiData {

	public int id;
	public String image;
	public String upDownStream;
	public double curRate;
	public double averageRate;
	public String status;
	public long totalTraffic;

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUpDownStream() {
		return upDownStream;
	}

	public void setUpDownStream(String upDownStream) {
		this.upDownStream = upDownStream;
	}

	public double getCurRate() {
		return curRate;
	}

	public void setCurRate(double curRate) {
		this.curRate = curRate;
	}

	public double getAverageRate() {
		return averageRate;
	}

	public void setAverageRate(double averageRate) {
		this.averageRate = averageRate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTotalTraffic() {
		return totalTraffic;
	}

	public void setTotalTraffic(long totalTraffic) {
		this.totalTraffic = totalTraffic;
	}

	@Override
	public String toString() {
		return "WifiData [id=" + id + ", image=" + image + ", upDownStream=" + upDownStream + ", curRate=" + curRate
				+ ", averageRate=" + averageRate + ", status=" + status + ", totalTraffic=" + totalTraffic + "]";
	}

	

}
