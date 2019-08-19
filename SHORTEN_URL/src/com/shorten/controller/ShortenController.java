package com.shorten.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.shorten.util.ShortenUtil;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ShortenController {
	
	Logger logger = LoggerFactory.getLogger(ShortenController.class);
		
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/")
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		return "ShortenUrlReg";
	}
	
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void redirect(@PathVariable String id, HttpServletResponse response) throws Exception {
        //final String url = redis.opsForValue().get(id);
        if (id != null) {
        	ShortenUtil u = new ShortenUtil();
        	String originUrlText = u.existOriginUrl(id);
        	logger.info("originUrlText : " + originUrlText);
        	response.sendRedirect(originUrlText);
        } else{
        	response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
	/**
	 * 
	 */
	@RequestMapping(value = "urlShorten", method=RequestMethod.POST)
	@ResponseBody
	public String getShortenUrl(
			HttpSession session, HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		
		
		logger.info("longUrl Text : " + (String)request.getParameter("urlText"));
		
		boolean urlValid = true;
		
		String urlText = (String)paramMap.get("urlText");
		
		ShortenUtil u = new ShortenUtil(8, request.getContextPath());
		String returnShortenUrl = u.existShortenUrl(urlText);

		// 1.�씠誘� 議댁옱�븯�뒗 URL�젙蹂댁씠硫� 湲곗〈�쓽 URL �젙蹂대�� 由ы꽩
		if(returnShortenUrl != null) {
			return returnShortenUrl;
		}
		 
		// 2. �쑀�슚�븯吏� �븡�� URL�씤 寃쎌슦 ALERT 泥섎━ �븳�떎.
		urlValid = u.validateUrlCheck(urlText);
		if(!urlValid) {
			return "-1";
		}
		
		// 3. �깉濡쒖슫 URL �씤寃쎌슦 �썝�옒URL怨� �떒異뷫RL�쓣 �뙆�씪�떆�뒪�뀥�뿉 �벑濡앺븳�떎.
		if(returnShortenUrl == null) {
			returnShortenUrl = u.shortenURL(urlText);
			logger.info(urlText + " -> " + returnShortenUrl);
			File file = new File(u.shortenUrlFileDrive,u.shortenUrlFileName);
		    BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(file, true));
				bw.write(urlText + "\t" + returnShortenUrl);
				bw.newLine();
	            bw.flush();					
			} catch (Exception e) {
		            e.getStackTrace();
			} finally {
	            try {
	                if(bw != null) bw.close();
	            } catch(IOException e) {
	                e.printStackTrace();
	            }
	        }
		}
		return returnShortenUrl;
	}	
	
}
