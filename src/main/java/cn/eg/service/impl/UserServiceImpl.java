package cn.eg.service.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.eg.entity.TbUser;
import org.springframework.stereotype.Service;

import cn.eg.mapper.PeopleMapper;
import cn.eg.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Resource
	private PeopleMapper pMapper;


	@Override
	public List<TbUser> findAll() {
		return null;
	}
}
