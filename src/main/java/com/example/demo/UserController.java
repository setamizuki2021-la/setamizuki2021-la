package com.example.demo;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
	@Autowired
	HttpSession session;

	/*
	 * ログイン画面の表示
	 */
	// ログイン画面をlogin.htmlに表示する
	@RequestMapping(value = "/")
	public String login() {
		// セッション画面を破棄する
		session.invalidate();
		return "login";
	}
}
