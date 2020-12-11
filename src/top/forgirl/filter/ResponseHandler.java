package top.forgirl.filter;

import com.alibaba.fastjson.JSON;
import top.forgirl.domain.ResultType;
import top.forgirl.domain.vo.ResultVo;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseHandler implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    // 设置相应头，返回json字符串
    //
    // 设置成为短连接
    // 设置跨域的响应头
    // 设置返回格式为json
    // 设置字符串编码为utf-8
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader("Connection", "close");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Method", "POST, GET, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, P-Token");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Cathe-Control", "no-cathe");
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        // 尝试捕获可能出现的exception,返回统一格式
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            response.getWriter()
                    .print(JSON.toJSONString(new ResultVo<String>(ResultType.FAILED, "服务器内部错误")));
        }

        httpServletResponse.flushBuffer();
    }

    @Override
    public void destroy() {

    }
}
