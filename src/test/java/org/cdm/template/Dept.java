package org.cdm.template;

import com.mhdt.annotation.Alias;
import com.mhdt.annotation.NoDB;

public class Dept {
	
	@NoDB
	int id;
	
	@Alias(value = "name")
	String 姓名;
	
	String code;
	
	String type;
	
	int sort;
	
	int creator;
	
	String createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String get姓名() {
		return 姓名;
	}

	public void set姓名(String 姓名) {
		this.姓名 = 姓名;
	}

	@Override
	public String toString() {
		return "Dept [id=" + id + ", 姓名=" + 姓名 + ", code=" + code + ", type=" + type + ", sort=" + sort + ", creator="
				+ creator + ", createTime=" + createTime + "]";
	}

}
