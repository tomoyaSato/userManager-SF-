package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Sort;
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
		List<UserInfo> thisUserInfo = userInfoRepository.findByIdAndPassword(Integer.parseInt(txtId), txtPassword);
		if(thisUserInfo.size() > 0){
			List<UserInfo> userInfoList = userInfoRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
//			List<UserInfo> userInfoList = userInfoRepository.findAll();
			mv.setViewName("userList");
			mv.addObject("UserInfo", userInfoList);
			return mv;
		}else{
	        mv.setViewName("login");
	        mv.addObject("id", txtId);
	        mv.addObject("password", "");
	        mv.addObject("loginError",true);
	        return mv;
		}
    }
	@RequestMapping(value="/logout")
	public ModelAndView logout(ModelAndView mv){
		mv.setViewName("login");
		return mv;
	}
	@RequestMapping(value = "/admin/index", method = RequestMethod.GET)
    public String menu() {
        return "index";
    }
	@RequestMapping(value = "/userListPost", params = "insert", method=RequestMethod.POST)
	public ModelAndView userListPostInsert(@RequestParam("selectUser") String selectUserId,
			ModelAndView mv){
		mv.setViewName("userInfoDetail");
		mv.addObject("title", "ユーザー情報登録");
		return mv;

	}
	@RequestMapping(value = "/userListPost", params = "update", method=RequestMethod.POST)
	public ModelAndView userListPostUpdate(@RequestParam("selectUser") String selectUserId,
			ModelAndView mv){
		mv.setViewName("userInfoDetail");
		mv.addObject("title", "ユーザー情報更新");
		return mv;

	}
	@RequestMapping(value = "/userListPost", params = "delete", method=RequestMethod.POST)
	public ModelAndView userListPostDelete(@RequestParam("selectUser") String selectUserId,
			ModelAndView mv){

		return mv;

	}
}
