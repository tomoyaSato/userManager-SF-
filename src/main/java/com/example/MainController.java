package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@EnableAutoConfiguration
public class MainController {

	@Autowired
    UserInfoRepository userInfoRepository;

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "Hello, Spring Boot Sample Application!";
    }

	@RequestMapping(value="/login")
    public ModelAndView login(ModelAndView mv)
    {
        mv.setViewName("login");
        mv.addObject("id", "");
        mv.addObject("password", "");
        mv.addObject("loginError",false);
        return mv;
    }

	@RequestMapping(value="/login", method=RequestMethod.POST)
    public ModelAndView indexPosted(
        @RequestParam("txtId") String txtId,
        @RequestParam("txtPassword") String txtPassword,
        ModelAndView mv
    ) {
		List<UserInfo> list = userInfoRepository.findByIdAndPassword(Integer.parseInt(txtId), txtPassword);
		if(list.size() > 0){
			return mv;
		}else{
	        mv.setViewName("login");
	        mv.addObject("id", txtId);
	        mv.addObject("password", "");
	        mv.addObject("loginError",true);
	        return mv;
		}
    }
	@RequestMapping(value = "/admin/index", method = RequestMethod.GET)
    public String menu() {
        return "index";
    }
}
