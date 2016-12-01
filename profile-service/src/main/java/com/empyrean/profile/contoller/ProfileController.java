package com.empyrean.profile.contoller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chirdeep on 29/11/2016.
 */

@RestController
public class ProfileController {

    @RequestMapping("/")
    public String index(){
        return "I am profile service";
    }

    @RequestMapping("/scope")
    @PreAuthorize("#oauth2.hasScope('WRITE')")
    public String scope() {
        return "You have a the required scope";
    }

    @RequestMapping("/authority")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public String authority() {
        return "You have a the required authority";
    }
}
