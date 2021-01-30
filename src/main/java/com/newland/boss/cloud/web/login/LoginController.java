package com.newland.boss.cloud.web.login;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	@Autowired
	private HttpSession session;

	/**
	 * 登陆
	 * 
	 * @param userName
	 * @param userPwd
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(String userName, String userPwd) {

		if (userName.equals("admin") && userPwd.equals("admin")) {
			// 将用户对象添加到Session中
			session.setAttribute("USER_SESSION", userName);
			return "true";
		}
		return "false";
	}

	/**
	 * 验证是否登陆
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getLoginUser", method = RequestMethod.POST)
	public String checkLogin() {

		String loginName = (String) session.getAttribute("USER_SESSION");
		if (loginName == null) {
			return "false";
		} else {
			return "true";
		}

	}
}
