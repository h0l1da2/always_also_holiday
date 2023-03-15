package today.also.hyuil.domain.member;

public enum Name {
    ROLE_HOLIDAY, ROLE_ADMIN, ROLE_USER;

    public static Name of(String name) {
        if (name.equals(ROLE_USER)) {
            return ROLE_USER;
        }
        if (name.equals(ROLE_ADMIN)) {
            return ROLE_ADMIN;
        }
        if (name.equals(ROLE_HOLIDAY)) {
            return ROLE_HOLIDAY;
        }
        return null;
    }
}
