package org.jeon.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import org.jeon.domain.NaverLoginBO;
import org.jeon.domain.NaverLoginApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.scribejava.core.model.OAuth2AccessToken;

@Controller
public class LoginController {
	private NaverLoginBO naverLoginBO;
	private String apiResult = null;
	
	@Autowired
	private void setNaverLoginBO(NaverLoginBO naverLoginBO) {
		this.naverLoginBO = naverLoginBO;
	}
	
	@RequestMapping(value = "/naverlogin", method = {RequestMethod.GET, RequestMethod.POST})
	public String login(Model model, HttpSession session) {
		String naverAuthUrl = naverLoginBO.getAuthorizational(session);
		
		System.out.println("네이버: " + naverAuthUrl);
		
		model.addAttribute("url", naverAuthUrl);
		
		return "naverLogin";
	}
	
	@RequestMapping(value= "/callback", method= {RequestMethod.GET, RequestMethod.POST}) 
	public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException {
		System.out.println("Callback!");
		OAuth2AccessToken oauthToken;
		oauthToken = naverLoginBO.getAccessToken(session, code, state);
		apiResult = naverLoginBO.getUserProfile(oauthToken);
		System.out.println(naverLoginBO.getUserProfile(oauthToken).toString());
		model.addAttribute("result", apiResult);
		System.out.println("result"+apiResult);
		
        return "naversuccess";

	}
}
