package top.forgirl.servlet.ClerkOperation;

import top.forgirl.domain.entity.Clerk;
import top.forgirl.domain.ResultType;
import top.forgirl.util.JDBCUtil;
import top.forgirl.util.ResponseUtil;
import top.forgirl.util.ValidateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/clerk/insert"})
public class ClerkInsertServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Clerk clerk = (Clerk) ResponseUtil.getBeanFromInputStream(req.getInputStream(), Clerk.class);

        System.out.println(clerk);
        if (!ValidateUtil.validateInsert(clerk)) {
            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.PARAM_ERROR, "请检查插入的信息"));
            return;
        }

//        int i = JDBCUtil.insertClerkMsg(clerk);
//
//        if (i > 0) {
//            resp.getWriter().print(ResponseUtil.generateFinalJson("成功插入" + i + "条数据"));
//        } else {
//            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.FAILED, "插入失败"));
//        }

        Clerk returnClerk = JDBCUtil.insertClerkMsgAndReturnClerk(clerk);

        if (returnClerk != null) {
            resp.getWriter().print(ResponseUtil.generateFinalJson(returnClerk));
        } else {
            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.FAILED, "插入失败"));
        }
    }
}
