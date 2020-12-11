package top.forgirl.util;

import top.forgirl.domain.dto.Principal;
import top.forgirl.domain.entity.Book;
import top.forgirl.domain.entity.Clerk;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtil {

    private static String driver = "com.mysql.cj.jdbc.Driver";
    //    private static String url = "jdbc:mysql://localhost:3306/book_management?" +
//            "useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8";
    private static String url = "jdbc:mysql://localhost:3306/demo?" +
            "useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8";
    private static String username = "root";
    private static String password = "root";

    private static Connection connection = null;

    private JDBCUtil() {
    }

    private static void init() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断账户名密码是否合法
     *
     * @param principal
     * @return success 唯一id ; fail -1
     */
    public static Clerk getClerkMsg(Principal principal) {
        String sql = "select `id`,`role`,`name`,`sex`,`phone_number`,`email`,`create_time`,`comment` from `clerk`" +
                " where `principal` = ? and `password` =? ";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Clerk clerk = null;

        if (connection == null) {
            init();
        }
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, principal.getPrincipal());
            preparedStatement.setString(2, principal.getPassword());

            System.out.println("执行登录查询的preparedStatement对象 : "+preparedStatement.toString());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                clerk = new Clerk();
                clerk.setId(resultSet.getInt("id"));
                clerk.setRole(resultSet.getString("role"));
                clerk.setName(resultSet.getString("name"));
                clerk.setSex(resultSet.getString("sex"));
                clerk.setPhoneNumber(resultSet.getString("phone_number"));
                clerk.setEmail(resultSet.getString("email"));
                clerk.setCreateDate(getLocalDateFormTime(resultSet.getTimestamp("create_time")));
                clerk.setComment(resultSet.getString("comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement);
            return clerk;
        }
    }

    //更新用户信息
    public static int updateClerkMsg(Clerk clerk) {
//        String sql = "update clerk set `name` = ? , `sex` = ? , `role` = ? , `password` = ? where `principal` = ?";
        StringBuilder sql = new StringBuilder("update clerk set ");
        PreparedStatement preparedStatement = null;
        int condition = 0;
        int result = 0;

        if (connection == null) {
            init();
        }
        try {

            if (ValidateUtil.validateName(clerk.getName())) {
                if (condition == 0) {
                    sql.append(" `name` = '" + clerk.getName() + "'");
                    condition++;
                } else {
                    sql.append(" , `name` = '" + clerk.getName() + "'");
                }
            }

            if (ValidateUtil.validateName(clerk.getSex())) {
                if (condition == 0) {
                    sql.append(" `sex` = '" + clerk.getSex() + "'");
                    condition++;
                } else {
                    sql.append(" , `sex` = '" + clerk.getSex() + "'");
                }
            }

            if (ValidateUtil.validateName(clerk.getRole())) {
                if (condition == 0) {
                    sql.append(" `role` = '" + clerk.getRole() + "'");
                    condition++;
                } else {
                    sql.append(" , `role` = '" + clerk.getRole() + "'");
                }
            }

            if (ValidateUtil.validatePassword(clerk.getPassword())) {
                if (condition == 0) {
                    sql.append(" `password` = '" + clerk.getPassword() + "'");
                    condition++;
                } else {
                    sql.append(" , `password` = '" + clerk.getPassword() + "'");
                }
            }

            if (condition != 0) {
                if (ValidateUtil.validatePrincipal(clerk.getPrincipal())) {
                    sql.append(" where `principal` = '" + clerk.getPrincipal() + "'");
                } else {
                    return 0;
                }
            } else {
                return 0;
            }

            System.out.println("update clerk : "+sql.toString());
            preparedStatement = connection.prepareStatement(sql.toString());
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
        }

        return result;
    }

    //插入用户信息
    public static int insertClerkMsg(Clerk clerk) {
        String sql = "insert into clerk(`role`,`principal`,`password`,`name`,`sex`,`phone_number`,`email`,`comment`,`create_time`)" +
                "values (?,?,?,?,?,?,?,?,?)";

        if (connection == null)
            init();
        int result = 0;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clerk.getRole());
            preparedStatement.setString(2, clerk.getPrincipal());
            preparedStatement.setString(3, clerk.getPassword());
            preparedStatement.setString(4, clerk.getName());
            preparedStatement.setString(5, clerk.getSex());
            if (clerk.getPhoneNumber() != null)
                preparedStatement.setString(6, clerk.getPhoneNumber());
            else
                preparedStatement.setString(6, "");

            if (clerk.getEmail() != null)
                preparedStatement.setString(7, clerk.getEmail());
            else
                preparedStatement.setString(7, "");

            if (clerk.getComment() != null)
                preparedStatement.setString(8, clerk.getComment());
            else
                preparedStatement.setString(8, "");

            if (clerk.getCreateDate() != null) {
                preparedStatement.setString(9, clerk.getCreateDate().toString());
            } else {
                preparedStatement.setString(9, LocalDate.now().toString());
            }

            System.out.println("insert clerk : "+sql);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
        }

        return result;
    }

    public static Clerk insertClerkMsgAndReturnClerk(Clerk clerk) {
        String sql = "select `id`,`principal`,`role`,`name`,`sex`,`phone_number`,`email`,`create_time`,`comment` from " +
                "`clerk` where `principal` = ?";
        PreparedStatement preparedStatement = null;
        Clerk returnClerk = new Clerk();
        ResultSet resultSet = null;

        int i = insertClerkMsg(clerk);

        if (i > 0) {
            if (connection == null) {
                init();
            }

            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, clerk.getPrincipal());

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    returnClerk.setId(resultSet.getInt("id"));
                    returnClerk.setPrincipal(resultSet.getString("principal"));
                    returnClerk.setRole(resultSet.getString("role"));
                    returnClerk.setName(resultSet.getString("name"));
                    returnClerk.setSex(resultSet.getString("sex"));
                    returnClerk.setPhoneNumber(resultSet.getString("phone_number"));
                    returnClerk.setEmail(resultSet.getString("email"));
                    returnClerk.setCreateDate(getLocalDateFormTime(resultSet.getTimestamp("create_time")));
                    returnClerk.setComment(resultSet.getString("comment"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                close(resultSet, preparedStatement);
            }

            return returnClerk;

        } else {
            return null;
        }

    }

    //删除用户信息
    public static int deleteClerkMsg(Clerk clerk) {
        StringBuilder sql = new StringBuilder("delete from `clerk` where 1=1 ");
        boolean flag = false;

        int result = 0;
        PreparedStatement preparedStatement = null;

        if (connection == null) {
            init();
        }

        try {
            if (clerk.getPrincipal() != null) {
                if (ValidateUtil.validatePrincipal(clerk.getPrincipal())) {
                    sql.append(" and `principal` = '" + clerk.getPrincipal() + "'");
                    flag = true;
                } else {
                    throw new RuntimeException("请检查条件");
                }
            }
            if (clerk.getName() != null) {
                if (ValidateUtil.validateName(clerk.getName())) {
                    sql.append(" and `name` = '" + clerk.getName() + "'");
                    flag = true;
                } else {
                    throw new RuntimeException("请检查条件");
                }
            }
            if (clerk.getSex() != null) {
                if (ValidateUtil.validateSex(clerk.getSex())) {
                    sql.append(" and `sex` = '" + clerk.getSex() + "'");
                    flag = true;
                } else {
                    throw new RuntimeException("请检查条件");
                }
            }
            if (clerk.getCreateDate() != null) {
                sql.append(" and `create_time` = '" + clerk.getCreateDate() + "'");
                flag = true;
            }

            System.out.println("delete clerk : "+sql.toString());
            preparedStatement = connection.prepareStatement(sql.toString());
            if (flag == true) {
                result = preparedStatement.executeUpdate();
                System.out.println(sql.toString());
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
        }

        return result;
    }

    //查询用户信息
    public static List<Clerk> queryClerkMsg(Clerk clerk) {
        StringBuilder sql = new StringBuilder("select `id`, `principal`,`role`,`name`,`sex`,`phone_number`,`email`,`create_time`,`comment`" +
                " from `clerk` where 1=1 ");
        List<Clerk> clerks = new ArrayList<>();
        Class<Clerk> clerkClass = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (connection == null) {
            init();
        }

        try {
            clerkClass = (Class<Clerk>) clerk.getClass();
            Field[] fields = clerkClass.getDeclaredFields();
            Field.setAccessible(fields, true);
            for (Field field : fields) {
                Object o = field.get(clerk);
                String fieldValue = new String();
                if (o != null) {
                    if (o instanceof String) {
                        fieldValue = (String) field.get(clerk);
                    }
                    if ("principal".equals(field.getName())) {
                        sql.append(" and `principal` = '" + fieldValue + "'");
                        continue;
                    }
                    if ("createTime".equals(field.getName())) {
                        sql.append(" and `create_time` = '" + o.toString() + "'");
                        continue;
                    }
                    if ("sex".equals(field.getName())) {
                        sql.append(" and `sex` = '" + fieldValue + "'");
                        continue;
                    }

                    if (fieldValue.length() != 0) {
                        sql.append(" and `" + field.getName() + "` like '%" + fieldValue + "%'");
                    }
                }
            }

            System.out.println("query clerk : "+sql.toString());
            preparedStatement = connection.prepareStatement(sql.toString());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Clerk clerkItem = new Clerk();
                clerkItem.setId(resultSet.getInt("id"));
                clerkItem.setPrincipal(resultSet.getString("principal"));
                clerkItem.setRole(resultSet.getString("role"));
                clerkItem.setName(resultSet.getString("name"));
                clerkItem.setSex(resultSet.getString("sex"));
                clerkItem.setPhoneNumber(resultSet.getString("phone_number"));
                clerkItem.setEmail(resultSet.getString("email"));
                clerkItem.setCreateDate(getLocalDateFormTime(resultSet.getTimestamp("create_time")));
                clerkItem.setComment(resultSet.getString("comment"));
                clerks.add(clerkItem);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement);
        }

        return clerks;
    }

    public static int insertBookMsg(Book book) {
        String sql = "INSERT INTO book ( `title`, `sequence_num`, `author`, `create_date`, `category`)values" +
                "(?, ?, ?, ?, ? )";
        PreparedStatement preparedStatement = null;
        int result = 0;

        if (connection == null) {
            init();
        }
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getSequenceNum());
            preparedStatement.setString(3, book.getAuthor());
            preparedStatement.setString(5, book.getCategory());
            if (book.getCreateDate() != null) {
                preparedStatement.setString(4, book.getCreateDate().toString());
            } else {
                preparedStatement.setString(4, LocalDate.now().toString());
            }

            System.out.println("执行插入的PreparedStatement对象 : "+preparedStatement.toString());
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
        }

        return result;
    }

    public static int deleteBookMsg(Book book) {
        StringBuilder sql = new StringBuilder("delete from `book` where 1 = 1");
        PreparedStatement preparedStatement = null;
        int condition = 0;
        int result = 0;

        if (book.getTitle() != null) {
            sql.append(" and `title` = '" + book.getTitle() + "'");
            condition++;
        }
        if (book.getSequenceNum() != null) {
            sql.append(" and `sequence_num` = '" + book.getSequenceNum() + "'");
            condition++;
        }
        if (book.getAuthor() != null) {
            sql.append(" and `author` = '" + book.getAuthor() + "'");
            condition++;
        }
        if (book.getCreateDate() != null) {
            sql.append(" and `create_date` = '" + book.getCreateDate() + "'");
            condition++;
        }
        if (book.getCategory() != null) {
            sql.append(" and `category` = '" + book.getCategory() + "'");
            condition++;
        }

        if (connection == null) {
            init();
        }
        try {
            if (condition != 0) {
                System.out.println("delete book : "+sql.toString());
                preparedStatement = connection.prepareStatement(sql.toString());
                result = preparedStatement.executeUpdate();
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
        }
        return result;
    }

    public static int updateBookMsg(Book book) {
        StringBuilder sql = new StringBuilder("update `book` set ");
        PreparedStatement preparedStatement = null;
        int condition = 0;
        int result = 0;

        if (book.getTitle() != null) {
            if (condition == 0) {
                sql.append(" `title` = '" + book.getTitle() + "'");
                condition++;
            } else {
                sql.append(" ,`title` = '" + book.getTitle() + "'");
            }
        }
        if (book.getAuthor() != null) {
            if (condition == 0) {
                sql.append(" `author` = '" + book.getAuthor() + "'");
                condition++;
            } else {
                sql.append(" , `author` = '" + book.getAuthor() + "'");
            }
        }
        if (book.getCreateDate() != null) {
            if (condition == 0) {
                sql.append(" and `create_date` = '" + book.getCreateDate() + "'");
                condition++;
            } else {
                sql.append(" , `create_date` = '" + book.getCreateDate() + "'");
            }
        }
        if (book.getCategory() != null) {
            if (condition == 0) {
                sql.append(" and `category` = '" + book.getCategory() + "'");
                condition++;
            } else {
                sql.append(" , `category` = '" + book.getCategory() + "'");
            }
        }
        if (condition != 0) {
            if (book.getSequenceNum() != null) {
                sql.append(" where `sequence_num` = '" + book.getSequenceNum() + "'");
            } else {
                return 0;
            }
        } else {
            return 0;
        }

        if (connection == null) {
            init();
        }

        try {
            System.out.println("update book : "+sql.toString());
            preparedStatement = connection.prepareStatement(sql.toString());
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
        }

        return result;
    }

    public static List<Book> queryBookMsg(Book book) {
        StringBuilder sql = new StringBuilder("select `id`,`title`,`sequence_num`,`author`,`create_date`,`category` from `book`" +
                "where 1 = 1");
        PreparedStatement preparedStatement = null;
        List<Book> books = new ArrayList<>();

        if (book.getTitle() != null) {
            sql.append(" and `title` like '%" + book.getTitle() + "%'");
        }
        if (book.getSequenceNum() != null) {
            sql.append(" and `sequence_num` like '%" + book.getSequenceNum() + "%'");
        }
        if (book.getAuthor() != null) {
            sql.append(" and `author` like '%" + book.getAuthor() + "%'");
        }
        if (book.getCreateDate() != null) {
            sql.append(" and `create_date` = '" + book.getCreateDate() + "'");
        }
        if (book.getCategory() != null) {
            sql.append(" and `category` like '%" + book.getCategory() + "%'");
        }

        if (connection == null) {
            init();
        }

        try {
            System.out.println("query book : "+sql.toString());
            preparedStatement = connection.prepareStatement(sql.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Book book1 = new Book();
                book1.setId(resultSet.getInt("id"));
                book1.setTitle(resultSet.getString("title"));
                book1.setSequenceNum(resultSet.getString("sequence_num"));
                book1.setAuthor(resultSet.getString("author"));
                book1.setCreateDate(getLocalDateFormTime(resultSet.getTimestamp("create_date")));
                book1.setCategory(resultSet.getString("category"));
                books.add(book1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
        }

        return books;
    }

    private static LocalDate getLocalDateFormTime(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.toLocalDate();
    }

    //关闭资源
    private static void close(ResultSet resultSet, PreparedStatement preparedStatement) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        if (connection!=null){
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private static void close(PreparedStatement preparedStatement) {
        close(null, preparedStatement);
    }
}
