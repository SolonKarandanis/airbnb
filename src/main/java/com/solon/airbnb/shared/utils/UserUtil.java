package com.solon.airbnb.shared.utils;

import com.solon.airbnb.user.domain.Authority;
import com.solon.airbnb.user.domain.User;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserUtil {

    // cannot create a new instance
    private UserUtil() {
    }

    /**
     * Check if any of the role names for this user matches the given role name
     *
     * @param u
     * @param role
     * @return true / false
     */
    public static boolean hasRole(User u, String role) {
        boolean result = false;
        if (u != null) {
            Set<Authority> userRoles = u.getAuthorities();
            for (Authority r : userRoles) {
                if (role.equals(r.getName())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * if user null, FALSE
     * if commaRoles empty/null TRUE
     * else check comma delimited ids to match user role ids.
     *
     * @param u
     * @param commaRoles
     * @return
     */
    public static boolean hasAnyRole(User u, String commaRoles) {
        boolean result = false;
        if (u != null && !StringUtils.hasLength(commaRoles)) {
            result = true; // no roles means success
        } else if (u != null) {

            String[] roles = commaRoles.split(",");
            Set<Authority> userRoles = u.getAuthorities();
            for (Authority r : userRoles) {
                for (int i = 0; i < roles.length; i++) {
                    if (roles[i].equalsIgnoreCase(r.getName())) {
                        result = true;
                    }
                }
                if (result) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @param u
     * @param commaRoles
     *            roles seperated by commas
     * @return Returns true is user's roles contains all roles in <code>commaRoles</code>
     * @author Argyriou
     */
    public static boolean hasAllRoles(User user, String commaRoles) {
        Collection<String> testRoles = Arrays.asList( commaRoles.split(","));
        Collection<String> userRoles = new HashSet<>();
        for (Authority role : user.getAuthorities()) {
            userRoles.add(role.getName());
        }
        return userRoles.containsAll(testRoles);
    }

    public static boolean hasAllRoles(User user, String... roles) {
        // concatenate roles and delimiter by
        // commas (save results into commaRoles)
        String commaRoles = "";
        StringBuilder buf = new StringBuilder();
        for (String role : roles) {
            buf.append(role + ",");
        }
        commaRoles = buf.toString();
        commaRoles = !commaRoles.isEmpty() ? commaRoles.substring(0, commaRoles.length() - 1) : "";
        return hasAllRoles(user, commaRoles);
    }






}
