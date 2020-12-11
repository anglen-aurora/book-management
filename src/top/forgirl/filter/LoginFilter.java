package top.forgirl.filter;

import com.alibaba.fastjson.JSON;
import top.forgirl.domain.ResultType;
import top.forgirl.domain.vo.ResultVo;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

public class LoginFilter implements Filter {
    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    //身份认证
    // 包含"/login"路径放行
    // 包含"P-Token"请求头，且头信息在servletContext域可查放行
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();
        String pass = filterConfig.getInitParameter("pass");
        HashMap<String, Integer> legalUser = (HashMap<String, Integer>) request.getServletContext().getAttribute("legalUser");
        String jSessionId = ((HttpServletRequest) request).getHeader("P-Token");

        if (requestURI.contains(pass) || legalUser.containsKey(jSessionId)) {
            chain.doFilter(request, response);
        } else {
            response.getWriter()
                    .print(JSON.toJSONString(new ResultVo<String>(ResultType.NON_LOGIN, "请先登录")));
        }
    }

    @Override
    public void destroy() {

    }

}
