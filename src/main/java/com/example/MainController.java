package com.example;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.validate.order.ValidateOrder;

@Controller
@EnableAutoConfiguration
public class MainController {

	@Autowired
    UserInfoRepository userInfoRepository;

	@ModelAttribute
	public UserInfoDetailFormCheck userInfoDetailFormCheck(){
		return new UserInfoDetailFormCheck();
	}

//    @RequestMapping("/")
//    @ResponseBody
//    public String home() {
//        return "Hello, Spring Boot Sample Application!";
//    }

	@RequestMapping(value="/")
	@ResponseBody
	public ModelAndView home(
			ModelAndView mv){
		mv.setViewName("login");
//		mv.addObject("loginError",false);
//		return mv;
		return indexPosted(mv);
	}

    /** ログイン **/
	@RequestMapping(value="/login")
	@ResponseBody
    public ModelAndView login(
    		Principal principal,
    		ModelAndView mv){
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
    		ModelAndView mv
    ) {
//		List<UserInfo> thisUserInfo = userInfoRepository
//				.findByIdAndPasswordAndDeleteFlg(Integer.parseInt(txtId), txtPassword,false);
//		if(thisUserInfo.size() > 0){
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		userInfoList = new ArrayList<UserInfo>(userInfoRepository.findByDeleteFlgOrderByIdAsc(false));
//		List<UserInfo> userInfoList = userInfoRepository.findAll();
		for(int i = 0;i < userInfoList.size();i++){
			UserInfo userinfo = new UserInfo(userInfoList.get(i));
			if(userinfo.getAuthority().equals("ROLE_ADMIN")){
//				userInfoList.get(i).setAuthority("管理者");
				userinfo.setAuthority("管理者");
			}else if(userinfo.getAuthority().equals("ROLE_USER")){
//				userInfoList.get(i).setAuthority("一般ユーザー");
				userinfo.setAuthority("一般ユーザー");
			}
			userInfoList.set(i, userinfo);
		}
		String headerUserInfo = getAuthorities();
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
//	@RequestMapping(value="/logout")
//	public ModelAndView logout(
//			Principal principal,
//			ModelAndView mv){
//		mv.setViewName("logout");
//		return mv;
//	}
	@RequestMapping(value = "/admin/index", method = RequestMethod.GET)
    public String menu() {
        return "index";
    }

	/** ユーザー情報一覧 **/
	@RequestMapping(value = "/userListPost", params = "insert", method=RequestMethod.POST)
	public ModelAndView userListPostInsert(
			Principal principal,
			ModelAndView mv){
		// ヘッダー部処理
		String headerUserInfo = getAuthorities();
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
			@RequestParam("selectUser") String selectId,
			UserInfoDetailFormCheck userInfoDetailFormCheck,
			Principal principal,
			ModelAndView mv){
		if(selectId.equals("")){
			// ユーザー未選択時処理
			mv.addObject("errorMessage", "ユーザーが未選択です。");
			return backFromUFI(principal,mv);
		}
		// ヘッダー部処理
		String headerUserInfo = getAuthorities();
		mv.addObject("headerUserInfo", headerUserInfo);
		mv.addObject("displayLogoutButton", true);

		// メイン部処理
		UserInfo thisUserInfo = userInfoRepository.findById(Integer.parseInt(selectId));
		mv.setViewName("userInfoDetail");
		mv.addObject("title", "ユーザー情報更新");
		mv.addObject("submitButtonName","update");
		mv.addObject("submitButtonValue", "更新");
		mv.addObject("showPasswordChangeCheckBox", true);
		mv.addObject("isDisabledUserId", "disabled");
		mv.addObject("isDisabledPassword", "disabled");
		mv.addObject("id",thisUserInfo.id);
		userInfoDetailFormCheck.setUserId(thisUserInfo.getUserId());
		userInfoDetailFormCheck.setName(thisUserInfo.getName());

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
			@RequestParam("selectUser") String selectId,
			Principal principal,
			ModelAndView mv){

		UserInfo thisUserInfo = userInfoRepository.findById(Integer.valueOf(selectId));
		if(thisUserInfo.getId() == 1){
			// 初期登録ユーザー削除エラー
			mv.addObject("errorMessage", "初期登録ユーザーは削除できません。");
			return backFromUFI(principal,mv);
		}
		// ヘッダー部処理
		String headerUserInfo = getAuthorities();
		mv.addObject("headerUserInfo", headerUserInfo);
		mv.addObject("displayLogoutButton", true);

		// マイン部処理
		Timestamp updateTimestamp = new Timestamp(System.currentTimeMillis());
		thisUserInfo.updateTimestamp = updateTimestamp;
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
		return indexPosted(mv);
	}

	@RequestMapping(value = "/userInfoDetail", params = "insert", method=RequestMethod.POST)
	public ModelAndView userInfoInsert(
	        @RequestParam("authorities") String authorities,
	        @Validated(ValidateOrder.class) UserInfoDetailFormCheck userInfoDetailFormCheck,
	        Errors errors,
	        Principal principal,
	        ModelAndView mv){
		// ヘッダー部処理
		String headerUserInfo = getAuthorities();
		mv.addObject("headerUserInfo", headerUserInfo);
		mv.addObject("displayLogoutButton", true);

		// メイン部処理
		if(errors.hasErrors()){
			// バリデートエラー処理
			return userListPostInsert(principal, mv);
		}
		UserInfo insertUserInfo = new UserInfo();
		Timestamp insertTimestamp = new Timestamp(System.currentTimeMillis());

		insertUserInfo.userId = userInfoDetailFormCheck.getUserId();
		// パスワードのハッシュ化
		String hashedPass = BCrypt.hashpw(userInfoDetailFormCheck.getPassword(), BCrypt.gensalt());

		insertUserInfo.setPassword(hashedPass);
		insertUserInfo.setName(userInfoDetailFormCheck.getName());
		if(authorities.equals("admin")){
			insertUserInfo.authority = "ROLE_ADMIN";
		}else if(authorities.equals("user")){
			insertUserInfo.authority = "ROLE_USER";
		}
		insertUserInfo.setCreateTimestamp(insertTimestamp);
		insertUserInfo.updateTimestamp = insertTimestamp;
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
			@RequestParam("txtId") String txtId,
	        @RequestParam("hiddenCheckboxChangePass") String hiddenCheckboxChangePass,
	        @RequestParam("authorities") String authorities,
	        @Validated(ValidateOrder.class) UserInfoDetailFormCheck userInfoDetailFormCheck,
	        Errors errors,
	        Principal principal,
	        ModelAndView mv){

		// バリデートエラー処理
		// パスワード未変更の場合はパスワードのチェック処理をしない
		if(isErrCheck(errors,hiddenCheckboxChangePass)){
			return userListPostUpdate(txtId,userInfoDetailFormCheck,principal, mv);
		}

		UserInfo thisUserInfo = userInfoRepository.findByUserId(userInfoDetailFormCheck.getUserId());

		Timestamp updateTimestamp = new Timestamp(System.currentTimeMillis());
		thisUserInfo.setName(userInfoDetailFormCheck.getName()) ;
		if(hiddenCheckboxChangePass.equals("on")){
			// パスワードのハッシュ化
			String hashedPass = BCrypt.hashpw(userInfoDetailFormCheck.getPassword(), BCrypt.gensalt());
			thisUserInfo.setPassword(hashedPass);
		}
		if(authorities.equals("admin")){
			thisUserInfo.authority = "ROLE_ADMIN";
		}else if(authorities.equals("user")){
			thisUserInfo.authority = "ROLE_USER";
		}
		thisUserInfo.updateTimestamp = updateTimestamp;
		userInfoRepository.save(thisUserInfo);

		// ヘッダー部処理
		String headerUserInfo = getAuthorities();
		mv.addObject("headerUserInfo", headerUserInfo);
		mv.addObject("displayLogoutButton", true);

		// メイン部処理
		mv.setViewName("insertUpdateComp");
		mv.addObject("title","更新完了");
		mv.addObject("message","ユーザー情報の更新が完了しました。");

		mv.addObject("userId",thisUserInfo.userId);
		mv.addObject("name",thisUserInfo.name);

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
	private String getAuthorities(){
		// 引数のPrincipalに含まれているauthoritiesを取得する
		UserInfo sessionUserInfo = (UserInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// ユーザー情報をDBから取得する
		UserInfo dbUserInfo = userInfoRepository.findByIdAndDeleteFlg(sessionUserInfo.getId(),false);

		// DBとセッションでユーザー情報に差異が有る場合は、DBに合わせセッション側を変更する
		// ユーザー名
		if(!sessionUserInfo.getName().equals(dbUserInfo.getName())){
			sessionUserInfo.setName(dbUserInfo.getName());
		}
		// パスワード
		if(!sessionUserInfo.getPassword().equals(dbUserInfo.getPassword())){
			sessionUserInfo.setPassword(dbUserInfo.getPassword());
		}
		// 権限
		if(!sessionUserInfo.getAuthority().equals(dbUserInfo.getAuthority())){
			sessionUserInfo.setAuthority(dbUserInfo.getAuthority());
		}
		// 削除フラグ
		if(!sessionUserInfo.getDeleteFlg() == dbUserInfo.getDeleteFlg()){
			sessionUserInfo.setDelete_flg(dbUserInfo.getDeleteFlg());
		}

		String headerUserInfo = "";

		// 取得したauthoritiesによってヘッダー部に出力する情報を変更する
		if(sessionUserInfo.getAuthority().equals("ROLE_ADMIN")){
			headerUserInfo += "管理者：" + sessionUserInfo.getName();
		}else if(sessionUserInfo.getAuthority().equals("ROLE_USER")){
			headerUserInfo += "一般ユーザー：" + sessionUserInfo.getName();
		}

		return headerUserInfo;
	}

	// エラー時にtrueを返す
	private boolean isErrCheck(Errors errors, String hiddenCheckboxChangePass){
		if(errors.hasErrors() == false){
			// そもそもエラーがない
			return false;
		}
		if(hiddenCheckboxChangePass.equals("off")){
			// パスワード変更のチェックが付いていない
			for(FieldError fielderror: errors.getFieldErrors()){
				if(fielderror.getField().equals("password") == false){
					// パスワード以外のエラーが有る
					return true;
				}
			}
			// パスワードにしかエラーがない
			return false;
		}
		return true;
	}
}