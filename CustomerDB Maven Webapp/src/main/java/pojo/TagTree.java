package pojo;

import net.sf.json.JSONArray;

public class TagTree {

	int id;
	int qid;
	String qname;
	String tree = new JSONArray().toString();
	
	int recordcount = 0;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public String getQname() {
		return qname;
	}
	public void setQname(String qname) {
		this.qname = qname;
	}
	public String getTree() {
		return tree;
	}
	public void setTree(String tree) {
		this.tree = tree;
	}
	
	public int getRecordcount() {
		return recordcount;
	}
	public void setRecordcount(int recordcount) {
		this.recordcount = recordcount;
	}
	
}
