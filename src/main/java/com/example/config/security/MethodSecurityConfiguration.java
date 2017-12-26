package com.example.config.security;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(jsr250Enabled =true)
/***
 *  @PreAuthorize(): pre-post-annotations prePostEnabled=true
 *  @RolesAllowed(): jsr250-annotations jsr250Enabled=true
 *  @Secured(): secured_annotation securedEnabled=true
 * 
 * */
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {
}
