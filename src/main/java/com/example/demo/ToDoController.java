package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ToDoController {
	@Autowired
	HttpSession session;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ToDoRepository todoRepository;

	/*
	 * 追加画面表示
	 */
	// 追加ボタンから追加画面を表示する
	@RequestMapping(value = "/add")
	public ModelAndView add(
			ModelAndView mv) {
		// add.htmlに遷移する。
		mv.setViewName("add");
		return mv;
	}

	/*
	 * 内容のリンクから編集画面を表示する。
	 */
	@RequestMapping(value = "/add/{code}")
	public ModelAndView edit(
			@PathVariable("code") Integer code,
			ModelAndView mv) {
		ToDo todo = todoRepository.findById(code).get();
		// ToDo内容をモデルにセット
		mv.addObject("code", code);
		mv.addObject("title", todo.getTitle());
		// add.htmlに表示
		mv.setViewName("add");
		return mv;
	}

	/*
	 * ToDo新規追加登録・編集 ボタン押下
	 */
	// セッションスコープからユーザーコードを取得
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String insert(
			@RequestParam("code") String code,
			@RequestParam("title") String title,
			ModelAndView mv) {
		// フォームの内容をToDoテーブルに登録する
		Integer usercode = (Integer) session.getAttribute("userCode");
		ToDo todo = null;
		// コードを受け取っていない（内容がない）場合は新規追加登録
		if (code == "" || code == null) {
			todo = new ToDo(usercode, title);
		} else {
			// コードを受け取った場合(ユーザーコードに内容が入っている)場合は編集登録
			todo = new ToDo(Integer.parseInt(code), usercode, title);
		}
		todoRepository.saveAndFlush(todo);
		// リダイレクト
		return "redirect:/list";
	}

	/*
	 * リスト一覧画面の表示
	 */
	@RequestMapping(value = "/list")
	public ModelAndView users(
			ModelAndView mv) {
		// セッションスコープからuser情報をうけとる
		Integer dologin = (Integer) session.getAttribute("userInfo");
		// userコードで、todoテーブルを検索
		List<ToDo> todo = todoRepository.findByUserCode(dologin);
		// ToDoの一覧画面(list.html)を表示する
		mv.setViewName("list");
		return mv;
	}

	/*
	 * 戻るボタン押下時にlist.htmlに戻る
	 */
	@RequestMapping(value = "/back")
	public String back() {
		return "list";
	}
}
