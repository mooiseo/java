package kr.co.samsung.emp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.samsung.common.util.DataSetBeanMapper;

@Controller
public class EmpController {

	@Autowired
    private DataSetBeanMapper datasetBeanMapper;
	
	
	@RequestMapping("EmpController.do")
	public void findEmpList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		System.out.println("@@@@@@@@@@@@컨트롤러다");
		
		
	}
	
}
