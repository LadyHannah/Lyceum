package com.lyceum.cms.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lyceum.cms.application.ReservationApplication;
import com.lyceum.cms.core.Reservation;
import com.zealyo.common.mvc.FreeMarkerController;
import com.zealyo.common.utils.StringUtil;
import com.zealyo.jdbc.util.PagingObject;

import net.sf.json.JSONObject;

/**
 * Reservation Controller
 * 
 * @author Hannah
 *
 */
@Controller
@RequestMapping("cms/reservation")
public class ReservationController extends FreeMarkerController{
	
	@Autowired
	private ReservationApplication reservationApplication;
	
	/**
	 * reservation index
	 */
	@Override
	@RequestMapping("")
	public String index(HttpServletRequest arg0, ModelMap arg1) {
		return getViewName("ftls/reservation/index");
	}
	
	/**
	 * Find Reservation List
	 * @param name
	 * @param language
	 * @param pagingObject
	 * @return
	 */
	@RequestMapping(value = "find", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject find(String name, String language, PagingObject pagingObject) {
		if (StringUtil.isNullOrEmpty(language)) {
			return Reservation.findGridResultByName(name, 0, pagingObject).toJsonObject();
		} else{
			return Reservation.findGridResultByName(name, Integer.parseInt(language), pagingObject).toJsonObject();
		}
	}
	
	/**
	 * Edit reservation info
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String editReservation(@PathVariable String id, ModelMap map) {
		Reservation po = null;
		if ("new".equals(id)) {
			po = new Reservation();
			map.put("po", po);
		} else {
			po = Reservation.get(id);
			map.put("po", po);
		}
		return getViewName("ftls/reservation/edit");
	}
	
	/**
	 * Save or Update Reservation Info
	 * @param po
	 * @return
	 */
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public boolean editReservation(Reservation po) {
		return reservationApplication.saveOrUpdate(po);
	}
	
	/**
	 * Save or Update Reservation Info
	 * @param po
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public boolean saveReservation(String name, String language, String email, String phone, String message) {
		Reservation po = new Reservation();
		po.setName(name);
		po.setLanguage(Integer.parseInt(language));
		po.setEmail(email);
		po.setPhone(phone);
		po.setMessage(message);
		return reservationApplication.saveOrUpdate(po);
	}
	
	/**
	 * Mass Delete
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteReservation(String ids) {
		return String.valueOf(reservationApplication.delete(ids));
	}
	
}
