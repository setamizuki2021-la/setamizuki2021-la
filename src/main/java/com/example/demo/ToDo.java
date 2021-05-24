package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "todo")
public class ToDo {
	@Id // 主キー
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "code")
	private Integer code;
	@Column(name = "title")
	private String title;
	@Column(name = "checked")
	private Boolean checked;
	@Column(name = "user_code")
	private Integer userCode;
	@Column(name = "category_code")
	private Integer categoryCode;

	// コンストラクタの生成
	public ToDo(Integer code, String title, Boolean checked, Integer userCode, Integer categoryCode) {
		this.code = code;
		this.title = title;
		this.checked = checked;
		this.userCode = userCode;
		this.categoryCode = categoryCode;
	}

	public ToDo(Integer userCode, String title) {
		this.userCode = userCode;
		this.title = title;
	}

	public ToDo(Integer code) {
		this.code = code;
	}

	public ToDo(Integer code, Integer userCode, String title) {
		this.code = code;
		this.userCode = userCode;
		this.title = title;
	}

	// デフォルトコンストラクタ
	public ToDo() {
	}
	// ゲッターの生成

	public Integer getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

	public Boolean getChecked() {
		return checked;
	}

	public Integer getUserCode() {
		return userCode;
	}

	public Integer getCategoryCode() {
		return categoryCode;
	}

}
