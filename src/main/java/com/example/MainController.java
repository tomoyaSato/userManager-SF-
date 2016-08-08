package com.example;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
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

	@RequestMapping(value="/")
	@ResponseBody
	public ModelAndView home(
			Principal principal,
			ModelAndView mv){
		mv.setViewName("login");
//		mv.addObject("loginError",false);
//		return mv;
		return indexPosted(principal,mv);
	}

    /** ログイン **/
	@RequestMapping(value="/login")
	@ResponseBody
    public ModelAndView login(
    		Principal principal,
    		ModelAndView mv)
    {
        mv.setViewName("login");
//        mv.addObject("userId", "");
//        mv.addObject("password", "");
        mv.addObject("loginError",false);
        return mv;
    }

	@RequestMapping(value="/userList", method=RequestMethod.POST)
    public ModelAndView indexPosted(
//        @RequestParam("txtId") String txtId,
//        @RequestParam("txtPassword") String txtPassword,
    		Principal principal,
    		ModelAndView mv
    ) {
//		List<UserInfo> thisUserInfo = userInfoRepository
//				.findByIdAndPasswordAndDeleteFlg(Integer.parseInt(txtId), txtPassword,false);
//		if(thisUserInfo.size() > 0){
			List<UserInfo> userInfoList = userInfoRepository.findByDeleteFlgOrderByIdAsc(false);
//			List<UserInfo> userInfoList = userInfoRepository.findAll();
			for(int i = 0;i < userInfoList.size();i++){
				if(userInfoList.get(i).getAuthority().equals("ROLE_ADMIN")){
					userInfoList.get(i).setAuthority("管理者");
				}else if(userInfoList.get(i).getAuthority().equals("ROLE_USER")){
					userInfoList.get(i).setAuthority("一般ユーザー");
				}
			}
			String headerUserInfo = getAuthorities(principal);
			mv.addObject("headerUserInfo", headerUserInfo);
			mv.addObject("displayLogoutButton", true);
			mv.addObject("UserInfo", userInfoList);
			mv.setViewName("userList");
			return mv;
//		}else{
//	        mv.setViewName("login");
//	        mv.addObject("id", txtId);
//	        mv.addObject("password", "");
//	        mv.addObject("loginError",true);
//	        return mv;
//		}
    }
	@RequestMapping(value="/logout")
	public ModelAndView logout(
			Principal principal,
			ModelAndView mv){
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
			Principal principal,
			ModelAndView mv){
		// ヘッダー部処理
		String headerUserInfo = getAuthorities(principal);
		mv.addObject("headerUserInfo", headerUserInfo);
		mv.addObject("displayLogoutButton", true);

		// メイン部処理
		mv.setViewName("userInfoDetail");
		mv.addObject("title", "ユーザー情報登録");
		mv.addObject("submitButtonName","insert");
		mv.addObject("submitButtonValue", "登録");
		mv.addObject("showPasswordChangeCheckBox", false);
		// 権限
		mv.addObject("adminSelected", "selected");

		return mv;

	}

	@RequestMapping(value = "/userListPost", params = "update", method=RequestMethod.POST)
	public ModelAndView userListPostUpdate(
			@RequestParam("selectUser") String selectUserId,
			Principal principal,
			ModelAndView mv){
		if(selectUserId.equals("")){
			// ユーザー未選択時処理
			mv.addObject("errorMessage", "ユーザーが未選択です。");
			return backFromUFI(principal,mv);
		}
		// ヘッダー部処理
		String headerUserInfo = getAuthorities(principal);
		mv.addObject("headerUserInfo", headerUserInfo);
		mv.addObject("displayLogoutButton", true);

		// メイン部処理
		UserInfo thisUserInfo = userInfoRepository.findByUserId(selectUserId);
		mv.setViewName("userInfoDetail");
		mv.addObject("title", "ユーザー情報更新");
		mv.addObject("submitButtonName","update");
		mv.addObject("submitButtonValue", "更新");
		mv.addObject("showPasswordChangeCheckBox", true);
		mv.addObject("isDisabledUserId", "disabled");
		mv.addObject("isDisabledPassword", "disabled");
		mv.addObject("userId",thisUserInfo.userId);
		mv.addObject("name",thisUserInfo.name);

		// 権限
		if(thisUserInfo.getAuthority().equals("ROLE_ADMIN")){
			mv.addObject("adminSelected", "selected");
		}else if(thisUserInfo.getAuthority().equals("ROLE_USER")){
			mv.addObject("userSelected", "selected");
		}

		return mv;
	}
	@RequestMapping(value = "/userListPost", params = "delete", method=RequestMethod.POST)
	public ModelAndView userListPostDelete(
			@RequestParam("selectUser") String selectUserId,
			Principal principal,
			ModelAndView mv){

		UserInfo thisUserInfo = userInfoRepository.findByUserId(selectUserId);
		if(thisUserInfo.getId() == 1){
			// 初期登録ユーザー削除エラー
			mv.addObject("errorMessage", "初期登録ユーザーは削除できません。");
			return backFromUFI(principal,mv);
		}
		// ヘッダー部処理
		String headerUserInfo = getAuthorities(principal);
		mv.addObject("headerUserInfo", headerUserInfo);
		mv.addObject("displayLogoutButton", true);

		// マイン部処理
		Timestamp insertTimestamp = new Timestamp(System.currentTimeMillis());
		thisUserInfo.update_timestamp = insertTimestamp;
		thisUserInfo.deleteFlg = true;
		userInfoRepository.save(thisUserInfo);

		mv.addObject("title","削除完了");
		mv.addObject("message","ユーザー情報の削除が完了しました。");

		mv.addObject("id",thisUserInfo.id);
		mv.addObject("name",thisUserInfo.name);
		mv.setViewName("deleteComp");
		return mv;
	}

	/** ユーザー情報詳細 **/
	@RequestMapping(value = "/userInfoDetail", params = "backPage", method=RequestMethod.POST)
	public ModelAndView backFromUFI(
			Principal principal,
			ModelAndView mv){
		return indexPosted(principal,mv);
	}

	@RequestMapping(value = "/userInfoDetail", params = "insert", method=RequestMethod.POST)
	public ModelAndView userInfoInsert(
			@RequestParam("txtUserId") String txtUserId,
			@RequestParam("txtPassword") String txtPassword,
	        @RequestParam("txtName") String txtName,
	        @RequestParam("authorities") String authorities,
	        Principal principal,
	        ModelAndView mv){
		// ヘッダー部処理
		String headerUserInfo = getAuthorities(principal);
		mv.addObject("headerUserInfo", headerUserInfo);
		mv.addObject("displayLogoutButton", true);

		// メイン部処理
		UserInfo insertUserInfo = new UserInfo();
		Timestamp insertTimestamp = new Timestamp(System.currentTimeMillis());
		// パスワードのハッシュ化
		String hashedPass = BCrypt.hashpw(txtPassword, BCrypt.gensalt());
		insertUserInfo.userId = txtUserId;
		insertUserInfo.name = txtName;
		insertUserInfo.password = hashedPass;
		if(authorities.equals("admin")){
			insertUserInfo.authority = "ROLE_ADMIN";
		}else if(authorities.equals("user")){
			insertUserInfo.authority = "ROLE_USER";
		}
		insertUserInfo.create_timestamp = insertTimestamp;
		insertUserInfo.update_timestamp = insertTimestamp;
		insertUserInfo.deleteFlg = false;
		userInfoRepository.save(insertUserInfo);

		mv.setViewName("insertUpdateComp");
		mv.addObject("title","登録完了");
		mv.addObject("message","ユーザー情報の登録が完了しました。");

		mv.addObject("userId",insertUserInfo.userId);
		mv.addObject("name",insertUserInfo.name);
		return mv;
	}

	@RequestMapping(value = "/userInfoDetail", params = "update", method=RequestMethod.POST)
	public ModelAndView userInfoUpdate(
			@RequestParam("txtUserId") String txtUserId,
			@RequestParam("txtPassword") String txtPassword,
	        @RequestParam("txtName") String txtName,
	        @RequestParam("hiddenCheckboxChangePass") String hiddenCheckboxChangePass,
	        @RequestParam("authorities") String authorities,
	        Principal principal,
	        ModelAndView mv){
		// ヘッダー部処理
		String headerUserInfo = getAuthorities(principal);
		mv.addObject("headerUserInfo", headerUserInfo);
		mv.addObject("displayLogoutButton", true);

		// メイン部処理
		UserInfo thisUserInfo = userInfoRepository.findByUserId(txtUserId);

		Timestamp updateTimestamp = new Timestamp(System.currentTimeMillis());
		thisUserInfo.name = txtName;
		if(hiddenCheckboxChangePass.equals("on")){
			// パスワードのハッシュ化
			String hashedPass = BCrypt.hashpw(txtPassword, BCrypt.gensalt());
			thisUserInfo.password = hashedPass;
		}
		if(authorities.equals("admin")){
			thisUserInfo.authority = "ROLE_ADMIN";
		}else if(authorities.equals("user")){
			thisUserInfo.authority = "ROLE_USER";
		}
		thisUserInfo.update_timestamp = updateTimestamp;
		userInfoRepository.save(thisUserInfo);

		mv.setViewName("insertUpdateComp");
		mv.addObject("title","更新完了");
		mv.addObject("message","ユーザー情報の更新が完了しました。");

		mv.addObject("userId",thisUserInfo.userId);
		mv.addObject("name",thisUserInfo.name);

		// メイン部処理
		UserInfo userInfo = new UserInfo();
		userInfo.getAuthorities();
		// ヘッダー部処理
		headerUserInfo = getAuthorities(principal);
		mv.addObject("headerUserInfo", headerUserInfo);
		mv.addObject("displayLogoutButton", true);



		return mv;
	}

	/** ユーザー情報登録・更新完了**/
	@RequestMapping(value = "/insertUpdateComp", method=RequestMethod.POST)
	public ModelAndView backFrominsertUpdateComp(
			Principal principal,
			ModelAndView mv){
		return backFromUFI(principal,mv);
	}

	// ユーザー情報から権限を取得する
	private String getAuthorities(Principal principal){
		// 引数のPrincipalに含まれているauthoritiesを取得する

		String headerUserInfo = "";

		String testStr = "";
		Object principalTest = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try{
			testStr = ((UserDetails) principalTest).getUsername();
		}catch(Exception Err){

		}

		// 名前の取得
		String principalStr =  principal.toString();
		int startIndex = principalStr.indexOf("Principal: ");
		startIndex += "Principal: [".length();
		principalStr = principalStr.substring(startIndex);
		int endIndex = principalStr.indexOf("];");
		principalStr = principalStr.substring(0,endIndex);
		String[] principalArray = principalStr.split(",");
		String name = "";
		for(String splitedPrincial:principalArray){
			String[] attribute = splitedPrincial.split(":");
			if(attribute[0].trim().equals("name")){
				name = attribute[1].trim();
				break;
			}
		}

		// 権限の取得
		principalArray = principal.toString().split(";");
		String authorities = null;
		for(String splitedPrincial:principalArray){
			String[] attribute = splitedPrincial.split(":");
			if(attribute[0].trim().equals("Granted Authorities")){
				authorities = attribute[1].trim();
				break;
			}
		}

		// 取得したauthoritiesによって出力する権限を変更する
		if(authorities.equals("ROLE_ADMIN")){
			headerUserInfo += "管理者：" + name;
		}else if(authorities.equals("ROLE_USER")){
			headerUserInfo += "一般ユーザー：" + name;
		}

		return headerUserInfo;
	}
}
