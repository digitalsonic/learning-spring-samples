package learning.spring.binarytea.support;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class RoleBasedJdbcUserDetailsManager extends JdbcUserDetailsManager {
    private Map<String, List<GrantedAuthority>> roleAuthoritiesMap = new HashMap<>();

    public RoleBasedJdbcUserDetailsManager() {
        roleAuthoritiesMap.put("ROLE_USER",
                AuthorityUtils.createAuthorityList("READ_MENU", "READ_ORDER"));
        roleAuthoritiesMap.put("ROLE_TEA_MAKER",
                AuthorityUtils.createAuthorityList("READ_MENU", "READ_ORDER", "WRITE_ORDER"));
        roleAuthoritiesMap.put("ROLE_MANAGER",
                AuthorityUtils.createAuthorityList("READ_MENU", "WRITE_MENU", "READ_ORDER", "WRITE_ORDER"));
    }

    @Override
    protected void addCustomAuthorities(String username, List<GrantedAuthority> authorities) {
        new ArrayList<>(authorities).stream()
                .filter(ga -> ga.getAuthority().toUpperCase().startsWith("ROLE_"))
                .forEach(r -> authorities.addAll(roleAuthoritiesMap.get(r.getAuthority())));
    }
}
