package cn.eg.controller;

import java.util.List;

import cn.eg.entity.People;
import cn.eg.entity.TbUser;
import cn.eg.mapper.PeopleMapper;
import cn.eg.mapper.TbUserMapper;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

	@Autowired
	private TbUserMapper mapper;
	@Autowired
	private PeopleMapper peopleMapper;

	private static final Logger logger = LoggerFactory.getLogger("");//空字符串会默认传到root logger上去。
	
	@RequestMapping("/test")
	public String test() {
	    /*TbUser user = new TbUser();
	    user.setId((long)7);
		user = mapper.selectOne(user);
        System.out.println(JSON.toJSONString(user));*/
	    int pageSize = 2;
		PageHelper.startPage(3,pageSize);
	    List<People> list = peopleMapper.selectAll();
	    list.forEach(p ->{
			System.out.println(JSON.toJSONString(p));
		});
		PageInfo<People> info = new PageInfo<>(list);
		/*System.out.println(info.getTotal());
		System.out.println(info.getPageSize());
		System.out.println(info.getPages());*/
		return "hello";

	}
}
