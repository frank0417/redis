package cn.wiesel.tool;
/**
 * @author 吴建
 * @version 1.7
 */
import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BaseServlet用来作为其它Servlet的父类
 * 
 * @author 吴建
 * 
 *  一个类多个请求处理方法，每个请求处理方法的原型与service相同！ 原型 = 返回值类型 + 方法名称 + 参数列表
 */
@SuppressWarnings("serial")
public class BaseServlet extends HttpServlet {

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		/**
		 * 1. 获取method参数，它是用户想调用的方法 2. 把方法名称变成Method类的实例对象 3. 通过invoke()来调用这个方法
		 */

		String methodName = request.getParameter("method");

		// 通过方法名获取Method的实例对象
		Method method = null;
		try {
//			System.out.println(this.getClass());//class wiesel.day02_1.UserServlet
			method = this.getClass().getMethod(methodName,
					HttpServletRequest.class, HttpServletResponse.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("您要调用的" + methodName + "它不存在！");
		}

		// 通过invoke()来调用这个方法
		try {
			String result = (String) method.invoke(this, request, response);

			// 如果请求处理方法不为空
			if (result != null && !result.trim().isEmpty()) {
				// 获取第一个冒号的位置
				int index = result.indexOf(":");
				// 如果没有冒号使用转发
				if (index == -1) {
					request.getRequestDispatcher(result).forward(request,
							response);
				}else {// 如果有冒号，以冒号为界进行处理方式和路径的截取
					String start = result.substring(0, index);// 分割出前缀
					String path = result.substring(index + 1);

					if (start.equals("f")) {// 前缀为f表示转发
						request.getRequestDispatcher(path).forward(request,
								response);
					} else if (start.equals("r")) {// 前缀为r表示重定向
						response.sendRedirect(request.getContextPath() + path);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
