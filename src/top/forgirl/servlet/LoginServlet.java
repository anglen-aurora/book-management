package top.forgirl.servlet;

import top.forgirl.domain.dto.Principal;
import top.forgirl.domain.entity.Clerk;
import top.forgirl.domain.ResultType;
import top.forgirl.domain.vo.RoleMsg;
import top.forgirl.util.JDBCUtil;
import top.forgirl.util.ResponseUtil;
import top.forgirl.util.ValidateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@WebServlet({"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //取参
        Principal p = (Principal) ResponseUtil.getBeanFromInputStream(req.getInputStream(), Principal.class);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        HashMap<String,Integer> legalUser = (HashMap<String, Integer>) req.getServletContext().getAttribute("legalUser");

        //校对
        System.out.println(p);
        if (!ValidateUtil.validateLogin(p)){
            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.PARAM_ERROR,"请检查账号和密码的格式"));
            return;
        }

        //取数据
        Clerk clerk = JDBCUtil.getClerkMsg(p);

        //输出
        if (clerk != null) {
            legalUser.put(uuid,clerk.getId());
            resp.getWriter().print(ResponseUtil.generateFinalJson(new RoleMsg(uuid,clerk.getRole())));
        } else {
            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.FAILED, "账号或密码错误"));
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}
