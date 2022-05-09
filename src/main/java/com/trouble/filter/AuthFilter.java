/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trouble.filter;

import beans.action.LoginActionBean;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Max
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/secured/*", "/faces/secured/*"})
public class AuthFilter implements Filter {

    @Inject
    private LoginActionBean login;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //HttpSession sess = req.getSession(false);
        //login = (LoginActionBean) sess.getAttribute("login");
        String bool = "false";
        for (Cookie c : req.getCookies())
        {
            if (c.getName().equals("isLoggedIn"))
            {
                bool = c.getValue();
                //System.out.println(bool);
            }
        }
        //System.out.println(Boolean.parseBoolean(bool));
        //System.out.println(login);
        //System.out.print(login.getLoggedIn());
        if (!Boolean.parseBoolean(bool))
        {
            System.out.println("User not logged in");
            res.sendRedirect(req.getContextPath() + "/loginpage.xhtml");

        }
        else
        {
            System.out.println("User logged in");
            chain.doFilter(request, response);

        }

    }

    @Override
    public void destroy()
    {
    }

}
