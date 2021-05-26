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
	public ModelAndView insert(
			@RequestParam("code") String code,
			@RequestParam("title") String title,
			ModelAndView mv) {
		// 更新・追加の内容が入力されていない（受け取っていない場合）はエラー表示
		if (title == null || title.length() == 0) {
			// エラーメッセージをセット
			mv.addObject("message", "未入力の項目があります");
			// コードをセット
			mv.addObject("code", code);
			// 更新・追加登録画面に遷移
			mv.setViewName("add");

			return mv;
		}
		// フォームの内容をToDoテーブルに登録する
		Integer usercode = (Integer) session.getAttribute("userInfo");
		ToDo todo = null;

		// コードを受け取っていない（内容がない）場合は新規追加登録
		if (code == "" || code == null) {
			todo = new ToDo(usercode, title);
		} else {
			// コードに紐づくtodo情報をdbから取得
			todo = todoRepository.findById(Integer.parseInt(code)).get();
			// titleだけセットしなおす
			todo.setTitle(title);
		}
		todoRepository.saveAndFlush(todo);
		// リダイレクト
		mv.setViewName("redirect:/list");
		return mv;
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
		List<ToDo> todo = todoRepository.findByUserCodeOrderByCodeAsc(dologin);

		mv.addObject("todo", todo);
		// ToDoの一覧画面(list.html)を表示する
		mv.setViewName("list");
		return mv;
	}

	/*
	 * 削除ボタン押下 指定したコードの削除処理
	 */
	// ToDoコードを受け取る
	@RequestMapping("/list/{code}/delete")
	public ModelAndView delete(
			@PathVariable(name = "code") Integer code,
			ModelAndView mv) {

		// 削除
		todoRepository.deleteById(code);

		// ToDoの一覧画面(list.html)を再表示する
		mv.setViewName("redirect:/list");
		return mv;
	}

	/*
	 * 戻るボタン押下時にlist.htmlに戻る
	 */
	@RequestMapping(value = "/back")
	public String back() {
		return "redirect:/list";
	}

	/*
	 * 完了ボタン押下に完了にしてlist.htmlに戻る
	 */
	@RequestMapping(value = "/check")
	public ModelAndView check(
			// codeの情報を取得
			@RequestParam("code") Integer code,
			ModelAndView mv) {
		// 指定したcodeのタスクの情報を取得
		ToDo todo = todoRepository.findById(code).get();
		// 取得したタスク情報のcheckedをTrueに設定
		todo.setChecked(true);
		// タスク情報をDBに更新
		todoRepository.saveAndFlush(todo);
		mv.addObject("check", todo.getCode());
		// リダイレクトしてlist.htmlに戻る
		mv.setViewName("redirect:/list");
		return mv;
	}

}
