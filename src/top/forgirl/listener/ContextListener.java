package top.forgirl.listener;

import top.forgirl.util.JDBCUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.HashSet;

public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("sadac5s6dv59v5wc56dsc5sd56asf5", 1);
        sce.getServletContext().setAttribute("legalUser", map);
    }

    // 在servletContext域中设置缓存，将发放给用户的uuid和登录用户的id存储下来
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        sce.getServletContext().setAttribute("legalUser",new HashMap<String,Integer>());
//    }

    // 删除servletContext域中缓存的用户登录信息
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("legalUser");
        JDBCUtil.destroy();
    }
}
