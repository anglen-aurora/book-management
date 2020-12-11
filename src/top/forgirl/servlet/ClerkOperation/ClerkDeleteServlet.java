package top.forgirl.servlet.ClerkOperation;

import top.forgirl.domain.entity.Clerk;
import top.forgirl.domain.ResultType;
import top.forgirl.util.JDBCUtil;
import top.forgirl.util.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/clerk/delete"})
public class ClerkDeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Clerk clerk = (Clerk) ResponseUtil.getBeanFromInputStream(req.getInputStream(), Clerk.class);

        System.out.println(clerk);

        int i = JDBCUtil.deleteClerkMsg(clerk);

        if (i >= 0) {
            resp.getWriter().print(ResponseUtil.generateFinalJson("成功删除"+i+"条数据"));
        }else {
            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.FAILED,"删除失败"));
        }

    }
}
