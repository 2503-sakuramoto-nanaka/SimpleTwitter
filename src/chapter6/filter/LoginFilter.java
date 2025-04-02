package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//●ユーザー編集画面とつぶやき編集画面にのみフィルターをかける
@WebFilter(urlPatterns = {"/setting","/edit"})
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		//●ServletRequestをHttpServletRequestへ型変換
		HttpServletRequest Request = (HttpServletRequest)request;
		//ServletResponseをHttpServletResponseへ型変換
		HttpServletResponse Response = (HttpServletResponse)response;

		HttpSession session = Request.getSession();
		List<String> errorMessages = new ArrayList<String>();

		//●ログインしていれば、リクエストにあった画面に遷移
		//●ログインしているかしていないかの確認は、
		//●セッション領域でログインユーザーの情報があるかないかで判断
		if (session.getAttribute("loginUser") != null){
			chain.doFilter(request, response); // サーブレットを実行
		//●ログインしていなければエラーメッセージを表示し、ログイン画面に遷移
		}else {
			errorMessages.add("ログインしてください");
			session.setAttribute("errorMessages", errorMessages);
			Response.sendRedirect("./login");
		}
	}

	//●initとdestoryは空でOK
	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}

}
