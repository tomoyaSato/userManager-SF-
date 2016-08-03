package com.example;

import java.sql.Timestamp;
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

//    @RequestMapping("/")
//    @ResponseBody
//    public String home() {
//        return "Hello, Spring Boot Sample Application!";
//    }

    /** ログイン **/
	@RequestMapping(value="/")
	@ResponseBody
    public ModelAndView login(ModelAndView mv)
    {
        mv.setViewName("login");
        mv.addObject("id", "");
        mv.addObject("password", "");
        mv.addObject("loginError",false);
        return mv;
    }

	@RequestMapping(value="/", method=RequestMethod.POST)
    public ModelAndView indexPosted(
        @RequestParam("txtId") String txtId,
        @RequestParam("txtPassword") String txtPassword,
        ModelAndView mv
    ) {
		List<UserInfo> thisUserInfo = userInfoRepository
				.findByIdAndPasswordAndDeleteFlg(Integer.parseInt(txtId), txtPassword,false);
		if(thisUserInfo.size() > 0){
			List<UserInfo> userInfoList = userInfoRepository.findByDeleteFlg(false);
//			List<UserInfo> userInfoList = userInfoRepository.findAll();
			mv.addObject("UserInfo", userInfoList);
			mv.setViewName("userList");
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

	/** ユーザー情報一覧 **/
	@RequestMapping(value = "/userListPost", params = "insert", method=RequestMethod.POST)
	public ModelAndView userListPostInsert(
			@RequestParam("selectUser") String selectUserId,
			ModelAndView mv){
		mv.setViewName("userInfoDetail");
		mv.addObject("title", "ユーザー情報登録");
		mv.addObject("submitButtonName","insert");
		mv.addObject("submitButtonValue", "登録");
		mv.addObject("showPasswordChangeCheckBox", false);
		return mv;

	}

	@RequestMapping(value = "/userListPost", params = "update", method=RequestMethod.POST)
	public ModelAndView userListPostUpdate(
			@RequestParam("selectUser") String selectUserId,
			ModelAndView mv){
		if(selectUserId.equals("")){
			// ユーザー未選択時処理
			mv.addObject("errorMessage", "ユーザーが未選択です。");
			return backFromUFI(mv);
		}
		List<UserInfo> thisUserInfo = userInfoRepository.findById(Integer.parseInt(selectUserId));
		mv.setViewName("userInfoDetail");
		mv.addObject("title", "ユーザー情報更新");
		mv.addObject("submitButtonName","update");
		mv.addObject("submitButtonValue", "更新");
		mv.addObject("showPasswordChangeCheckBox", true);
		mv.addObject("isDisabledPassword", "disabled");
		mv.addObject("id",thisUserInfo.get(0).id);
		mv.addObject("name",thisUserInfo.get(0).name);
		return mv;
	}
	@RequestMapping(value = "/userListPost", params = "delete", method=RequestMethod.POST)
	public ModelAndView userListPostDelete(
			@RequestParam("selectUser") String selectUserId,
			ModelAndView mv){

		UserInfo updateUserInfo = new UserInfo();
		List<UserInfo> thisUserInfo = userInfoRepository.findById(Integer.parseInt(selectUserId));

		updateUserInfo = thisUserInfo.get(0);
		Timestamp insertTimestamp = new Timestamp(System.currentTimeMillis());
		updateUserInfo.update_timestamp = insertTimestamp;
		updateUserInfo.deleteFlg = true;
		userInfoRepository.save(updateUserInfo);

		mv.addObject("title","削除完了");
		mv.addObject("message","ユーザー情報の削除が完了しました。");

		mv.addObject("id",updateUserInfo.id);
		mv.addObject("name",updateUserInfo.name);
		mv.setViewName("deleteComp");
		return mv;
	}

	/** ユーザー情報詳細 **/
	@RequestMapping(value = "/userInfoDetail", params = "backPage", method=RequestMethod.POST)
	public ModelAndView backFromUFI(ModelAndView mv){
		List<UserInfo> userInfoList = userInfoRepository.findByDeleteFlg(false);
		mv.addObject("UserInfo", userInfoList);
		mv.setViewName("userList");
		return mv;
	}

	@RequestMapping(value = "/userInfoDetail", params = "insert", method=RequestMethod.POST)
	public ModelAndView userInfoInsert(
			@RequestParam("txtPassword") String txtPassword,
	        @RequestParam("txtName") String txtName,
	        ModelAndView mv){
		UserInfo insertUserInfo = new UserInfo();
		Timestamp insertTimestamp = new Timestamp(System.currentTimeMillis());
		insertUserInfo.name = txtName;
		insertUserInfo.password = txtPassword;
		insertUserInfo.create_timestamp = insertTimestamp;
		insertUserInfo.update_timestamp = insertTimestamp;
		insertUserInfo.deleteFlg = false;
		userInfoRepository.save(insertUserInfo);

		mv.setViewName("insertUpdateComp");
		mv.addObject("title","登録完了");
		mv.addObject("message","ユーザー情報の登録が完了しました。");

		mv.addObject("id",insertUserInfo.id);
		mv.addObject("name",insertUserInfo.name);
		return mv;
	}

	@RequestMapping(value = "/userInfoDetail", params = "update", method=RequestMethod.POST)
	public ModelAndView userInfoUpdate(
			@RequestParam("txtId") String txtId,
			@RequestParam("txtPassword") String txtPassword,
	        @RequestParam("txtName") String txtName,
	        @RequestParam("hiddenCheckboxChangePass") String hiddenCheckboxChangePass,
	        ModelAndView mv){
		UserInfo updateUserInfo = new UserInfo();
		List<UserInfo> thisUserInfo = userInfoRepository.findById(Integer.parseInt(txtId));

		updateUserInfo = thisUserInfo.get(0);
		Timestamp insertTimestamp = new Timestamp(System.currentTimeMillis());
		updateUserInfo.name = txtName;
		if(hiddenCheckboxChangePass.equals("on")){
			updateUserInfo.password = txtPassword;
		}
		updateUserInfo.update_timestamp = insertTimestamp;
		userInfoRepository.save(updateUserInfo);

		mv.setViewName("insertUpdateComp");
		mv.addObject("title","更新完了");
		mv.addObject("message","ユーザー情報の更新が完了しました。");

		mv.addObject("id",updateUserInfo.id);
		mv.addObject("name",updateUserInfo.name);
		return mv;
	}

	/** ユーザー情報登録・更新完了**/
	@RequestMapping(value = "/insertUpdateComp", method=RequestMethod.POST)
	public ModelAndView backFrominsertUpdateComp(ModelAndView mv){
		return backFromUFI(mv);
	}
}
