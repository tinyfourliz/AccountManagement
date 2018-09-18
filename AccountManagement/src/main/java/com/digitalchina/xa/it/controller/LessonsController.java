package com.digitalchina.xa.it.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.digitalchina.xa.it.model.LessonDetailDomain;
import com.digitalchina.xa.it.service.LessonDetailService;
import com.digitalchina.xa.it.util.Encrypt;
import com.digitalchina.xa.it.util.EncryptImpl;

@Controller
@RequestMapping(value = "/lessons")
public class LessonsController {
	@Autowired
	private LessonDetailService lessonDetailService;
	
	//更新用户最新阅读的章节及最近阅读时间
	@ResponseBody
	@GetMapping("/updateChapter")
	public Object updateChapter(
	        @RequestParam(name = "param", required = true) String jsonValue){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Encrypt encrypt = new EncryptImpl();
    	String decrypt = null;
		try {
			decrypt = encrypt.decrypt(jsonValue);
		} catch (Exception e1) {
			e1.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", "解密失败！");
			return modelMap;
		}
    	String data = null;
		try {
			data = URLDecoder.decode(decrypt, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", "解密失败！非utf-8编码。");
			return modelMap;
		}
    	System.err.println("解密的助记词，密码及itcode的JSON为:" + data);
    	JSONObject jsonObj = JSONObject.parseObject(data);
		String itcode = jsonObj.getString("itcode");
		String chapter = jsonObj.getString("chapter");
		String lessonId = jsonObj.getString("lessonId");
		String backup1 = chapter.split("_")[0];
		
		String backup1_ = lessonDetailService.selectBackup1(itcode, Integer.parseInt(lessonId));
		int res = Integer.parseInt(backup1) - Integer.parseInt(backup1_);
		if( res <= 5 && res >=0) {
			LessonDetailDomain ld = new LessonDetailDomain();
			ld.setItcode(itcode);
			ld.setChapter(chapter);
			ld.setLessonId(Integer.parseInt(lessonId));
			ld.setRecentTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			ld.setBackup1(backup1);
			lessonDetailService.updateChapterAndRecentTime(ld);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "skippingReading");
		}
		
		return modelMap;
	}
	
	//插入用户首次阅读的课程编号及itcode
	@ResponseBody
	@GetMapping("/insertItcode")
	public Object insertItcode(
	        @RequestParam(name = "param", required = true) String jsonValue){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Encrypt encrypt = new EncryptImpl();
    	String decrypt = null;
		try {
			decrypt = encrypt.decrypt(jsonValue);
		} catch (Exception e1) {
			e1.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", "解密失败！");
			return modelMap;
		}
    	String data = null;
		try {
			data = URLDecoder.decode(decrypt, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(); 
			modelMap.put("success", false);
			modelMap.put("errMsg", "解密失败！非utf-8编码。");
			return modelMap;
		}
    	System.err.println("解密的助记词，密码及itcode的JSON为:" + data);
    	JSONObject jsonObj = JSONObject.parseObject(data);
		String itcode = jsonObj.getString("itcode");
		String lesson = jsonObj.getString("lesson");
		
		//FIXME 暂时修改课程学习重复记录itcode的bug 
		//此处暂时添加第三个参数，lessonid
		String lessonid = jsonObj.getString("lessonid");
		//LessonDetailDomain ld = lessonDetailService.selectOneRecord(itcode, lesson);
		//System.out.println(ld);
		Integer counter = lessonDetailService.selectLessonAndItcodeRecord(itcode, Integer.valueOf(lessonid));
		System.err.println("counter : " + counter);
		if(counter < 1){
			lessonDetailService.insertItcode(itcode, lesson);
		}
		modelMap.put("success", true);
		
		return modelMap;
	}
	
	//查询每一个科目的阅读人数
	@ResponseBody
	@GetMapping("/getCount")
	public Object getCount(
	        @RequestParam(name = "param", required = true) String jsonValue){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Encrypt encrypt = new EncryptImpl();
    	String decrypt = null;
		try {
			decrypt = encrypt.decrypt(jsonValue);
		} catch (Exception e1) {
			e1.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", "解密失败！");
			return modelMap;
		}
    	String data = null;
		try {
			data = URLDecoder.decode(decrypt, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", "解密失败！非utf-8编码。");
			return modelMap;
		}
    	System.err.println("解密的助记词，密码及itcode的JSON为:" + data);
    	JSONObject jsonObj = JSONObject.parseObject(data);
    	for(String key : jsonObj.keySet()) {
    		Integer count = lessonDetailService.selectOrderCount(jsonObj.getString(key));
    		modelMap.put(key, count);
    	}
		modelMap.put("success", true);
		
		return modelMap;
	}
}
