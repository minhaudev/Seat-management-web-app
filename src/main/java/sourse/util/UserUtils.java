package sourse.util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
public class UserUtils {
    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                System.out.println("Role: " + authority.getAuthority());

                if (authority.getAuthority().equals("ROLE_SUPERUSER")) {
                    return "SUPERUSER";
                } else if (authority.getAuthority().equals("ROLE_LANDLORD")) {
                    return "LANDLORD";
                }
            }
        }
        return "UNKNOWN";
    }
}
