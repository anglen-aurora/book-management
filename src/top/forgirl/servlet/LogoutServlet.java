package top.forgirl.servlet;

import top.forgirl.domain.ResultType;
import top.forgirl.domain.dto.Principal;
import top.forgirl.util.ResponseUtil;
import top.forgirl.util.ValidateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebServlet({"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Principal principal = (Principal) ResponseUtil.getBeanFromInputStream(req.getInputStream(), Principal.class);
        HashMap<String, String> legalUser = (HashMap<String, String>) req.getServletContext().getAttribute("legalUser");
        String key = null;

        if (!ValidateUtil.validatePrincipal(principal.getPrincipal())) {
            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.PARAM_ERROR, "请检查参数"));
            return;
        }


        if (legalUser.containsValue(principal.getPrincipal())) {
            Set<Map.Entry<String, String>> entries = legalUser.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                if (entry.getValue().equals(principal.getPrincipal())) {
                    key = entry.getKey();
                }
            }

            if (legalUser.remove(key, principal.getPrincipal())) {
                resp.getWriter().print(ResponseUtil.generateFinalJson("成功退出账号"));
            } else {
                resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.FAILED, "退出失败，请重新尝试"));
            }
        } else {
            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.FAILED, "请检查是否已登录"));
        }

    }

}
