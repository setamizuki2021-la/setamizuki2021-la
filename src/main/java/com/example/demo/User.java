package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id // 主キー
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_code")
	private Integer userCode;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "password")
	private String password;

//コンストラクタの生成
	public User(Integer userCode, String userId, String password) {
		this.userCode = userCode;
		this.userId = userId;
		this.password = password;
	}

	public User(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

//デフォルトコンストラクタ
	public User() {
	}

	public Integer getUserCode() {
		return userCode;
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

}
