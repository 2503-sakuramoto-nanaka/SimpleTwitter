package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		String messageId = request.getParameter("message_id");
		//●getParameterで取得してきた値は全てString型だが、idはint型なので型変換
		int messageIdInt = Integer.parseInt(messageId);
		//●MessageServiceのdeleteに引数としてint型のmassageIdIntを追加
		//●setAttributeでJSPへ値を渡す()
		Message message = new MessageService().edit(messageIdInt);
		//●"message"という名前にmessageの情報をrequestに格納
		request.setAttribute("message", message);
		//●requestに格納したmessageの情報と
		request.getRequestDispatcher("/edit.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();
		//●エラーメッセージを格納するリストを表示
		List<String> errorMessages = new ArrayList<String>();

		String text = request.getParameter("text");
		
		if (!isValid(text, errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		Message message = new Message();
		message.setText(text);
		

		//？MessageServletでは、どのようにつかわれていたのか（なその時ぜ必要だったのか）→ここでも必要か確認
		User user = (User) session.getAttribute("loginUser");
		message.setUserId(user.getId());

		new MessageService().update(message);
		response.sendRedirect("./");
	}

	private boolean isValid(String text, List<String> errorMessages) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		if (StringUtils.isEmpty(text)) {
			errorMessages.add("メッセージを入力してください");
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}

		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}
