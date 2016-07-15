package com.example;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class loginController {
	@RequestMapping("/")
	public String index(Model model) {
        ArrayList<ViewData> list = new ArrayList<ViewData>();
        for (int i = 0; i < 5; i++) {
            ViewData data = new ViewData();
            StringBuffer buffer = new StringBuffer();
            buffer.append("メッセージ");
            buffer.append(i);
            data.setNo(i + 1);
            data.setMessage(buffer.toString());
            list.add(data);
        }

        model.addAttribute("msg", list);
        return "index";
    }

	@RequestMapping(value="/login")
    public ModelAndView login(ModelAndView mv)
    {
        mv.setViewName("login");
        mv.addObject("id", "");
        mv.addObject("password", "");
        return mv;
    }

	@RequestMapping(value="/login", method=RequestMethod.POST)
    public ModelAndView indexPosted(
        @RequestParam("txtId") String txtId,
        @RequestParam("txtPassword") String txtPassword,
        ModelAndView mv
    ) {
        mv.setViewName("login");
        mv.addObject("id", txtId);
        mv.addObject("password", txtPassword);
        return mv;
    }
}

class ViewData {
    private int no;
    private String message;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return message;
    }
}
