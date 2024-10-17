package com.solon.airbnb.user.application.utils;

import com.solon.airbnb.user.domain.Authority;
import com.solon.airbnb.user.domain.User;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserUtil {
    private UserUtil() {
    }

    /**
     * Check if any of the authority names for this user matches the given role name
     *
     * @param u
     * @param authority
     * @return true / false
     */
    public static boolean hasAuthority(User u, String authority){
        boolean result = false;
        if (u != null){
            Set<Authority> authorities= u.getAuthorities();
            for(Authority a: authorities){
                if(authority.equals(a.getName())){
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Retrieves users emails as single string. Emails are delimited with ,
     *
     * @param users
     * @return
     */
    public static String getEmailsStringForUsers(List<User> users) {
        List<String> emailsList = getEmails(users);
        String emails = emailsList.stream().collect(Collectors.joining (","));
        return emails;
    }

    public static List<String> getEmails(List<User> users) {
        List<String> emailsList = new ArrayList<>();

        for (User user : users) {
            if(StringUtils.hasLength(user.getEmail())) {
                emailsList.add(user.getEmail());
            }
        }

        return emailsList;
    }
}
