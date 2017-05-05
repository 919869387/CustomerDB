package pojo;

import java.sql.Timestamp;

/*
 * 对应datastore数据表
 */
public class QuestionData {

	int id;
	String data;
	int qid;
	String customerid;
	boolean integrated = true;
	Timestamp recordtime;

	public Timestamp getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(Timestamp recordtime) {
		this.recordtime = recordtime;
	}
	public boolean isIntegrated() {
		return integrated;
	}

	public void setIntegrated(boolean integrated) {
		this.integrated = integrated;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}


}
