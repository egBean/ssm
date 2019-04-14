package cn.eg.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.eg.entity.People;
import cn.eg.mapper.PeopleMapper;
import cn.eg.service.Service1;

@Service
public class Service1Impl implements Service1{
	
	@Resource
	private PeopleMapper pMapper;

	@Override
	public List<People> findAll() {
		
		return pMapper.selectAll();
	}

}
