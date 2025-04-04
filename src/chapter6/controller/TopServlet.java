package chapter6.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.User;
import chapter6.beans.UserComment;
import chapter6.beans.UserMessage;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class TopServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public TopServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
		" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		boolean isShowMessageForm = false;
		User user = (User) request.getSession().getAttribute("loginUser");
		if (user != null) {
			isShowMessageForm = true;
		}
		/*
		 * String型のuser_idの値をrequest.getParameter("user_id")で
		 * JSPから受け取るように設定
		 * MessageServiceのselectに引数としてString型のuser_idを追加
		 */
		String userId = request.getParameter("user_id");
		//●開始時刻と現在時刻のStartとendの値をJSPから受け取る
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		/*DBから取得してきたUserMessage型のアイテムがリスト状に格納されている*/
		List<UserMessage> messages = new MessageService().select(userId, start, end);
		List<UserComment> comments = new CommentService().select();

		/*UserMessage型のアイテムをJSPで表示できるよう、requestにset*/
		request.setAttribute("messages", messages);
		request.setAttribute("isShowMessageForm", isShowMessageForm);
		request.setAttribute("comment",comments );
		request.setAttribute("start", start);
		request.setAttribute("end", end);
		request.getRequestDispatcher("/top.jsp").forward(request, response);
	}
}