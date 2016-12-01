package com.empyrean.auth.service;

import com.empyrean.auth.model.Privilege;
import com.empyrean.auth.repository.AccountRepository;
import com.empyrean.auth.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chirdeep on 18/11/2016.
 */

@Service
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username)
                .map(account -> {
                    Collection<GrantedAuthority> authorities = getAuthorities(account.getRoles());
                    return new User(account.getUsername(), account.getPassword(), authorities);
                })
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
    }

    private Collection<GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
        ArrayList<Privilege> collection = new ArrayList<>();
        for(Role role : roles){
            collection.addAll(role.getPrivileges());
        }
        privileges.addAll(collection.stream().map(Privilege::getName).collect(Collectors.toList()));
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        return privileges.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
