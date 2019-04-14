package cn.eg.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.eg.entity.People;
import cn.eg.service.Service1;

@Controller
public class MyController {

	@Resource
	private Service1 serviceImpl;
	
	@RequestMapping("/test")
	public String test() {
		/*List<People> list=serviceImpl.findAll();
		for(People people:list) {
			System.out.println(people.toString());
		}*/
		System.out.println("zzzzz");
		
		return "hello";
	}
}
