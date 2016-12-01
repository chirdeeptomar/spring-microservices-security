package com.empyrean.auth.factory;

import com.empyrean.auth.model.Privilege;
import com.empyrean.auth.model.Role;
import com.empyrean.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by Chirdeep on 19/11/2016.
 */

@Component
public class RoleFactory {

    private static RoleRepository roleRepository = null;

    @Autowired
    public RoleFactory(RoleRepository privilegeRepository) {
        RoleFactory.roleRepository = privilegeRepository;
    }

    public static Role create(String name, Collection<Privilege> privileges){
        Role role = new Role(name);
        role.setPrivileges(privileges);
        roleRepository.save(role);
        return role;
    }
}
