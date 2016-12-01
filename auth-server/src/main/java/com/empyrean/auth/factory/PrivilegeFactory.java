package com.empyrean.auth.factory;

import com.empyrean.auth.model.Privilege;
import com.empyrean.auth.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Chirdeep on 19/11/2016.
 */

@Component
public class PrivilegeFactory {

    private static PrivilegeRepository privilegeRepository = null;

    @Autowired
    public PrivilegeFactory(PrivilegeRepository privilegeRepository) {
        PrivilegeFactory.privilegeRepository = privilegeRepository;
    }

    public static Privilege create(String name){
        Privilege p = new Privilege(name);
        privilegeRepository.save(p);
        return p;
    }
}
