package top.forgirl.servlet.BookOperation;

import com.sun.org.apache.regexp.internal.RE;
import top.forgirl.domain.ResultType;
import top.forgirl.domain.entity.Book;
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

@WebServlet({"/book/query"})
public class BookQueryServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Book book = (Book) ResponseUtil.getBeanFromInputStream(req.getInputStream(), Book.class);

        System.out.println(book);
        if (!ValidateUtil.validateUncertainParam2(book)) {
            resp.getWriter().print(ResponseUtil.generateFinalJson(ResultType.PARAM_ERROR, "请检查参数"));
            return;
        }

        List<Book> books = JDBCUtil.queryBookMsg(book);

        resp.getWriter().print(ResponseUtil.generateFinalJson(books));

    }
}
