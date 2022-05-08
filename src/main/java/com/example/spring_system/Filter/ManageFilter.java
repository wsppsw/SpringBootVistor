package com.example.spring_system.Filter;

import com.example.spring_system.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "myFilter2", urlPatterns = "/manage/*")
public class ManageFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //把一般的请求对象request强转成HttpServletRequest
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        //获得当前会话对象
        HttpSession session = req.getSession();
         User admin = (User)session.getAttribute("admin");

        if(admin == null){
            //HttpServletResponse resp = (HttpServletResponse)servletResponse;
            HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) servletResponse);
            System.out.println("过滤器启动了");
            wrapper.sendRedirect("/management");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
