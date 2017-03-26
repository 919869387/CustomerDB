package pojo;

import net.sf.json.JSONArray;

public class Tag {

	Integer id;
	String cnname;
	String type; //标签值的显示属性 eg.文本框、多选框
	Integer beused_times = 0;//表示标签被使用的次数
	int parent_id = 0;
	String son_ids = new JSONArray().toString();

	public String getCnname() {
		return cnname;
	}

	public void setCnname(String cnname) {
		this.cnname = cnname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBeused_times() {
		return beused_times;
	}

	public void setBeused_times(Integer beused_times) {
		this.beused_times = beused_times;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public String getSon_ids() {
		return son_ids;
	}

	public void setSon_ids(String son_ids) {
		this.son_ids = son_ids;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
