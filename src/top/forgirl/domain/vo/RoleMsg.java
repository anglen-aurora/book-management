package top.forgirl.domain.vo;

public class RoleMsg {
    private String uuid;
    private String role;

    public RoleMsg(String uuid, String role) {
        this.uuid = uuid;
        this.role = role;
    }

    @Override
    public String toString() {
        return "RoleMsg{" +
                "uuid='" + uuid + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
