package cn.eg.controller;

import java.util.List;

import cn.eg.entity.People;
import cn.eg.entity.TbUser;
import cn.eg.mapper.PeopleMapper;
import cn.eg.mapper.TbUserMapper;
import com.alibaba.fastjson.JSON;
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

	private static final Logger logger = LoggerFactory.getLogger("userLog");
	
	@RequestMapping("/test")
	public String test() {
	    /*TbUser user = new TbUser();
	    user.setId((long)7);
		user = mapper.selectOne(user);
        System.out.println(JSON.toJSONString(user));*/

	    List<People> list = peopleMapper.selectAll();
	    list.forEach(p ->{
			System.out.println(JSON.toJSONString(p));
		});
		return "hello";

	}
}
