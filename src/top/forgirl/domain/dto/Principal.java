package top.forgirl.domain.dto;

public class Principal {
    private String principal;
    private String password;

    @Override
    public String toString() {
        return "Principal{" +
                "principal='" + principal + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
