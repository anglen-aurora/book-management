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

@WebServlet({"/clerk/update"})
public class ClerkUpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //取参
        Clerk clerk = (Clerk) ResponseUtil.getBeanFromInputStream(req.getInputStream(), Clerk.class);
        //校对
        System.out.println(clerk);

        //取数据
        int i = JDBCUtil.updateClerkMsg(clerk);

        //输出
        if (i > 0) {
            resp.getWriter().print(ResponseUtil.generateFinalJson("成功修改" + i + "条数据"));
        } else {
            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.FAILED, "修改失败"));
        }

    }

}
