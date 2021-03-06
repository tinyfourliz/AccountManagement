package com.digitalchina.xa.it.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.digitalchina.xa.it.model.LessonDetailDomain;
import com.digitalchina.xa.it.service.PaidVoteDetailService;
import com.digitalchina.xa.it.service.PaidVoteTop10Service;
import com.digitalchina.xa.it.service.PaidVoteTopicService;
import com.digitalchina.xa.it.util.Encrypt;
import com.digitalchina.xa.it.util.EncryptImpl;

@Controller
@RequestMapping(value = "/paidVotes")
public class PaidVoteController {
	@Autowired
	private PaidVoteDetailService paidVoteDetailService;
	@Autowired
	private PaidVoteTop10Service paidVoteTop10Service;
	@Autowired
	private PaidVoteTopicService paidVoteTopicService;
	
	@ResponseBody
	@GetMapping("/insertVoteDetail")
	public Object voteToSomebody(
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
    	System.err.println("解密后参数为：" + data);
    	JSONObject jsonObj = JSONObject.parseObject(data);
    	String toaccount = jsonObj.getString("toaccount");
    	String fromaccount = jsonObj.getString("fromaccount");
    	String toitcode = jsonObj.getString("toitcode");
    	String fromitcode = jsonObj.getString("fromitcode");
    	String turncount = jsonObj.getString("turncount");
    	String remark = jsonObj.getString("remark");
    	Integer topicId = jsonObj.getInteger("topicId");
		
    	paidVoteDetailService.voteToSomebody(toaccount, fromaccount, toitcode, fromitcode, turncount, remark, topicId);
//		if( res <= 5 && res >=0) {
//			modelMap.put("success", true);
//		} else {
//			modelMap.put("success", false);
//			modelMap.put("errMsg", "skippingReading");
//		}
		
		return modelMap;
	}
	
	@ResponseBody
	@GetMapping("/top5")
	public String getTop5(@RequestParam(name = "topicId", required = true) Integer topicId) {
		List<Map<String, Object>> dataList = paidVoteDetailService.selectTop10(topicId);
		List<Map<String, Object>> returnList = new ArrayList<>();
		for(int i = 0; i < dataList.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			Integer value = ((BigDecimal) dataList.get(i).get("balance")).intValue();
			//new BigDecimal((Double)dataList.get(i).get("balance"))
			
			map.put("key", dataList.get(i).get("itcode"));
			map.put("value", value);
			returnList.add(map);
		}
		String data = JSON.toJSONString(returnList);
		return data;
	}
}
