package com.digitalchina.xa.it.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalchina.xa.it.dao.LessonDetailDAO;
import com.digitalchina.xa.it.model.LessonDetailDomain;
import com.digitalchina.xa.it.service.LessonDetailService;

@Service(value = "LessonDetailService")
public class LessonDetailServiceImpl implements LessonDetailService {
	@Autowired
	private LessonDetailDAO lessonDetailDAO;

	@Override
	public Boolean insertItcode(String itcode,String lesson) {
		if(itcode != null && itcode !="" && lesson != null && lesson !="") {
			try {
				int effectedNumber = lessonDetailDAO.insertItcode(itcode, lesson);
				if(effectedNumber > 0) {
					return true;
				} else {
					throw new RuntimeException("插入itcode，lesson失败");
				}
			} catch(Exception e) {
				throw new RuntimeException("插入itcode，lesson失败 " + e.getMessage());
			}
		} else {
			throw new RuntimeException("插入 itcode，lesson不能为空");
		}
	}

	@Override
	public Integer selectOrderCount(String lesson) {
		return lessonDetailDAO.selectOrderCount(lesson);
	}

	@Override
	public LessonDetailDomain selectOneRecord(String itcode, String lesson) {
		return lessonDetailDAO.selectOneRecord(itcode, lesson);
	}
}