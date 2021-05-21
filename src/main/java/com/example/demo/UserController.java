package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
	@Autowired
	HttpSession session;
	@Autowired
	UserRepository userRepository;

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

	/*
	 * ログインを実行
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView dologin(
			@RequestParam("userId") String userId,
			@RequestParam("password") String password,
			ModelAndView mv) {
		// idが空の場合はエラーメッセージ
		if (userId == null || userId.length() == 0 || password == null || password.length() == 0) {
			mv.addObject("message", "正しく入力してください。");
			mv.setViewName("login");
			return mv;
		}
		// すべての情報をとってくる
		List<User> users = userRepository.findByUserIdAndPassword(userId, password);
		// idとパスワードが存在したらログインOK
		if (users.size() > 0) {
			// リストの1件目をログインユーザとして取得する
			User user = users.get(0);
			session.setAttribute("userInfo", user.getUserCode());

			// セッションスコープにカテゴリ情報を格納する
			session.setAttribute("users", userRepository.findAll());
			// list.htmlを表示する
			mv.setViewName("redirect:/list");
			return mv;

		} else {
			// 入力内容 が見つからなかった場合はログインNG
			// エラーメッセージをセット
			mv.addObject("message", "未登録の会員情報です。");
			// login.html（ログイン）を表示する
			mv.setViewName("login");
			return mv;
		}
	}

	/*
	 * アカウント新規作成画面の表示
	 */
	// リンクから新規登録画面を表示する
	@RequestMapping(value = "/new")
	public String newuser() {
		return "newUser";
	}

	/*
	 * アカウント新規作成
	 */
	// ユーザIDとパスワードを受け取ってusersテーブルに登録する。
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public ModelAndView addUser(
			@RequestParam("userId") String userId,
			@RequestParam("password") String password,
			ModelAndView mv) {
		// 未入力チェック
		if (userId == null || userId.length() == 0
				|| password == null || password.length() == 0) {

			// 会員登録画面に遷移
			mv.setViewName("newUser");
			// エラーメッセージをセット
			mv.addObject("message", "未入力の項目があります");
			return mv;
		}

		// セッションスコープから情報を取得
		@SuppressWarnings("unchecked")
		List<User> users = (List<User>) session.getAttribute("users");
		if (users == null) {
			// 空のユーザリストを生成
			users = new ArrayList<User>();
			// セッションスコープに保存
			session.setAttribute("users", users);
		}

		// 登録済みユーザIDのチェック
		for (User user : users) {
			if (userId.equals(user.getUserId())) {
				// 会員登録画面に遷移
				mv.setViewName("newUser");
				// エラーメッセージをセット
				mv.addObject("message", "登録済みのユーザIDです");
				return mv;
			}
		}

		// パラメータからオブジェクトを生成
		User user = new User(userId, password);
		// customerテーブルへの登録
		userRepository.saveAndFlush(user);
		// login.html(ログイン画面)を表示
		mv.setViewName("login");
		return mv;
	}

	/**
	 * ログアウトを実行
	 */
	@RequestMapping("/logout")
	public String logout() {
		// ログイン画面表示処理を実行するだけ
		return login();
	}

}
