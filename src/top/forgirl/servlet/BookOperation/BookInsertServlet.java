package top.forgirl.servlet.BookOperation;

import top.forgirl.domain.ResultType;
import top.forgirl.domain.entity.Book;
import top.forgirl.domain.vo.ResultVo;
import top.forgirl.util.JDBCUtil;
import top.forgirl.util.ResponseUtil;
import top.forgirl.util.ValidateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/book/insert"})
public class BookInsertServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Book book = (Book) ResponseUtil.getBeanFromInputStream(req.getInputStream(), Book.class);

        System.out.println(book);
        if (!ValidateUtil.validateInsert(book)) {
            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.PARAM_ERROR, "请检查参数"));
            return;
        }

        int result = JDBCUtil.insertBookMsg(book);

        if (result > 0){
            resp.getWriter().print(ResponseUtil.generateFinalJson("成功插入"+result+"条数据"));
        }else {
            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.FAILED,"插入失败"));
        }

    }
}
