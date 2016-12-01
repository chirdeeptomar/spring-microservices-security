package com.empyrean.auth.misc;

import com.empyrean.auth.factory.RoleFactory;
import com.empyrean.auth.model.Account;
import com.empyrean.auth.model.Privilege;
import com.empyrean.auth.model.Role;
import com.empyrean.auth.repository.AccountRepository;
import com.empyrean.auth.repository.PrivilegeRepository;
import com.empyrean.auth.repository.RoleRepository;
import com.empyrean.auth.factory.PrivilegeFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by chirdeep on 18/11/2016.
 */

@Component
public class SeedData implements CommandLineRunner {

    private AccountRepository accountRepository;
    private RoleRepository roleRepository;
    private PrivilegeRepository privilegeRepository;

    SeedData(AccountRepository accountRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        List<Privilege> nonAdminPrivileges = Collections.singletonList(readPrivilege);

        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", nonAdminPrivileges);

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        Role userRole = roleRepository.findByName("ROLE_USER").get();

        Stream.of("alan,test,admin", "chirdeep,123123,user").map(x -> x.split(","))
                .forEach(tuple -> {
                    Account user = new Account();
                    user.setUsername(tuple[0]);
                    user.setPassword(new BCryptPasswordEncoder().encode(tuple[1]));
                    user.setEmail(tuple[0]);
                    user.setRoles(tuple[2].equalsIgnoreCase("admin") ? Collections.singletonList(adminRole) : Collections.singletonList(userRole));
                    user.setActive(true);
                    this.accountRepository.save(user);
                });
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {
        return privilegeRepository.findByName(name)
                .map(privilege -> privilege)
                .orElse(PrivilegeFactory.create(name));
    }

    @Transactional
    private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        return roleRepository.findByName(name)
                .map(role -> role)
                .orElse(RoleFactory.create(name, privileges));
    }
}