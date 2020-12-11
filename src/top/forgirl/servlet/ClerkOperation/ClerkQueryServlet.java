package top.forgirl.servlet.ClerkOperation;

import top.forgirl.domain.ResultType;
import top.forgirl.domain.entity.Clerk;
import top.forgirl.util.JDBCUtil;
import top.forgirl.util.ResponseUtil;
import top.forgirl.util.ValidateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet({"/clerk/query"})
public class ClerkQueryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Clerk clerk = (Clerk) ResponseUtil.getBeanFromInputStream(req.getInputStream(), Clerk.class);

        System.out.println(clerk);
        if (!ValidateUtil.validateQuery(clerk)) {
            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.PARAM_ERROR, "请检查查询条件"));
            return;
        }

        List<Clerk> clerks = JDBCUtil.queryClerkMsg(clerk);

        resp.getWriter().print(ResponseUtil.generateFinalJson(clerks));

    }
}
