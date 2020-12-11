package top.forgirl.util;

import top.forgirl.domain.dto.Principal;
import top.forgirl.domain.entity.Book;
import top.forgirl.domain.entity.Clerk;

import java.util.HashSet;
import java.util.Objects;

public class ValidateUtil {

    private static HashSet<String> roles = new HashSet<>();
    private static HashSet<String> sexes = new HashSet<>();

    static {
        roles.add("admin");
        roles.add("general");
        sexes.add("男");
        sexes.add("女");
    }

    private ValidateUtil() {
    }

    public static boolean validatePrincipal(String principal) {

        if (!Objects.isNull(principal)) {
            if (principal.matches("^[0-9]{7}$")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean validatePassword(String password) {
        if (!Objects.isNull(password)) {
            if (password.matches("^[0-9a-zA-Z]{8,16}$")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static boolean validateRole(String role) {
        if (!Objects.isNull(role)) {
            if (roles.contains(role)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean validateName(String name) {
        if (!Objects.isNull(name)) {
            if (name.matches("^[a-zA-Z\u4e00-\u9fa5]+$")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean validateSex(String sex) {
        if (!Objects.isNull(sex)) {
            if (sexes.contains(sex)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if (!Objects.isNull(phoneNumber)) {
            if (phoneNumber.matches("^[0-9]{11}$")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

//    private static boolean validateEmail(String email){
//        if (!Objects.isNull(email)){
//            if (email.matches("^[a-zA-Z0-9]{8,16}@[a-zA-Z0-9]+\\.com$")){
//                return true;
//            }else {
//                return false;
//            }
//        }else {
//            return false;
//        }
//    }

//    private static boolean validateCreateTime(String createTime){
//        return true;
//    }

    public static boolean validateComment(String comment) {
        if (!Objects.isNull(comment)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateSequenceNum(String sequenceNum) {
        return validatePrincipal(sequenceNum);
    }

    public static boolean validateTitle(String title) {
        if (!Objects.isNull(title)) {
            if (title.matches("^[0-9a-zA-Z\u4e00-\u9fa5]+$")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean validateAuthor(String author) {
        return validateName(author);
    }

    public static boolean validateCategory(String category) {
        if (!Objects.isNull(category)) {
            if (category.matches("^[\u4e00-\u9fa5]{3}$")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //验证登录接口的参数
    public static boolean validateLogin(Principal principal) {
        if (validatePrincipal(principal.getPrincipal()) && validatePassword(principal.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    //验证插入接口
    public static boolean validateInsert(Clerk clerk) {
        if (validateRole(clerk.getRole()) && validatePrincipal(clerk.getPrincipal()) && validatePassword(clerk.getPassword())) {
            if (validateName(clerk.getName()) && validateSex(clerk.getSex())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean validateQuery(Clerk clerk) {
        boolean isTrue = true;
        if (clerk.getPrincipal() != null) {
            if (!validatePrincipal(clerk.getPrincipal())) {
                isTrue = false;
            }
        }
        if (clerk.getSex() != null) {
            if (!validateSex(clerk.getSex())) {
                isTrue = false;
            }
        }
        return isTrue;
    }

    public static boolean validateInsert(Book book) {
        if (validateTitle(book.getTitle()) && validateSequenceNum(book.getSequenceNum()) && validateAuthor(book.getAuthor())) {
            if (validateCategory(book.getCategory())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean validateUncertainParam(Book book) {
        if (!Objects.isNull(book.getTitle())) {
            if (!validateTitle(book.getTitle())) {
                return false;
            }
        }

        if (!Objects.isNull(book.getSequenceNum())) {
            if (!validateSequenceNum(book.getSequenceNum())) {
                return false;
            }
        }

        if (!Objects.isNull(book.getAuthor())) {
            if (!validateAuthor(book.getAuthor())) {
                return false;
            }
        }

        if (!Objects.isNull(book.getCategory())) {
            if (!validateCategory(book.getCategory())) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateUncertainParam2(Book book) {
        if (!Objects.isNull(book.getTitle())) {
            if (!validateTitle(book.getTitle())) {
                return false;
            }
        }

        if (!Objects.isNull(book.getSequenceNum())) {
            if (!book.getSequenceNum().matches("^[0-9]+$")) {
                return false;
            }
        }

        if (!Objects.isNull(book.getAuthor())) {
            if (!validateAuthor(book.getAuthor())) {
                return false;
            }
        }

        if (!Objects.isNull(book.getCategory())) {
            if (!validateCategory(book.getCategory())) {
                return false;
            }
        }
        return true;
    }

}
