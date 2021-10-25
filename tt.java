package com.atguigu.mvc.controller;

import com.atguigu.mvc.controller.bean.Employee;
import com.atguigu.mvc.controller.dao.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Date:2021/7/5
 * Author:ybc
 * Description:
 */
@Controller
public class HelloController {

    //  通过@RequestMapping注解，可以通过请求路径匹配要处理的具体的请求
    //  /表示的当前工程的上下文路径
    @Autowired
    private EmployeeDao employeeDao;
    @RequestMapping(value="/")
    public String index(){
        return "index";
    }
    @RequestMapping(value="/employee",method=RequestMethod.GET)
    public String getAllEmployee(Model model) {
        Collection<Employee> employeelist = employeeDao.getAll();
        model.addAttribute("employeelist",employeelist);
        System.out.println(employeelist);
        return "employee";
    }
    @RequestMapping(value="/toAdd",method=RequestMethod.GET)
    public String employeeadd(){
        return "employee_add";
    }
    @RequestMapping(value="/employee",method=RequestMethod.POST)
    public String employeesave(Employee employee){
        employeeDao.save(employee);
        return "index";
    }

    @RequestMapping(value="/employee/{id}",method=RequestMethod.GET)
    public String getEmployeeById(@PathVariable("id")Integer id,Model model){
        Employee employee=employeeDao.get(id);
        model.addAttribute("employee",employee);
        return "employee_update";
    }

    @RequestMapping(value = "/employee", method = RequestMethod.PUT)
    public String updateEmployee(Employee employee){
        employeeDao.save(employee);
        return "redirect:/employee";
    }

    @RequestMapping("/testDown/{id}")
    public ResponseEntity<byte[]> testResponseEntity(HttpSession session,@PathVariable("id") Integer Id) throws IOException {
        //获取ServletContext对象
        ServletContext servletContext = session.getServletContext();
        //获取服务器中文件的真实路径
        String realPath = servletContext.getRealPath("/static/img/"+Id+".jpg");
        //创建输入流
        InputStream is = new FileInputStream(realPath);
        //创建字节数组
        byte[] bytes = new byte[is.available()]; //将流读到字节数组中
        is.read(bytes);
        //创建HttpHeaders对象设置响应头信息
        MultiValueMap<String, String> headers = new HttpHeaders();
        //设置要下载方式以及下载文件的名字
        headers.add("Content-Disposition", "attachment;filename=1.jpg");
        //设置响应状态码
        HttpStatus statusCode = HttpStatus.OK;
        //创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode);
        //关闭输入流
        is.close();
        return responseEntity;
    }
}
