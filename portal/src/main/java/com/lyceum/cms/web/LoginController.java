package com.lyceum.cms.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zealyo.common.mvc.FreeMarkerController;

@Controller
public class LoginController extends FreeMarkerController{
	
	@Override
	public String index(HttpServletRequest arg0, ModelMap arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * login verification
	 * @param map
	 * @param error
	 * @param logout
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(ModelMap map,
			@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout
			) {
		if (error != null) {
			map.put("exception", "Invalid username and password!");
        }

        if (logout != null) {
        	map.put("exception", "You've been logged out successfully.");
        }
		return getViewName("ftls/login");
	}

	/**
	 * login error & logout
	 */
/*    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");

        return model;

    }*/

}
