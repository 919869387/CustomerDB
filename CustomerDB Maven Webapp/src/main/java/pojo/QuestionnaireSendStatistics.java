package pojo;

public class QuestionnaireSendStatistics {

	int id;
	String customerid;
	int  totalsendnum=0;
	int successsendnum=0;
	int answernum=0;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public int getTotalsendnum() {
		return totalsendnum;
	}
	public void setTotalsendnum(int totalsendnum) {
		this.totalsendnum = totalsendnum;
	}
	public int getSuccesssendnum() {
		return successsendnum;
	}
	public void setSuccesssendnum(int successsendnum) {
		this.successsendnum = successsendnum;
	}
	public int getAnswernum() {
		return answernum;
	}
	public void setAnswernum(int answernum) {
		this.answernum = answernum;
	}



}
