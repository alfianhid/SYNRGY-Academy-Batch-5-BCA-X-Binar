package com.binarxbca.chapter6.config;

import com.binarxbca.chapter6.model.enums.ERoles;
import com.binarxbca.chapter6.model.Role;
import com.binarxbca.chapter6.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleConfig {
    private static final Logger LOG = LoggerFactory.getLogger(RoleConfig.class);

    RoleConfig(RoleRepository roleRepository) {
        LOG.info("Cheking roles presented...");
        for(ERoles c : ERoles.values()) {
            try {
                Role roles = roleRepository.findByName(c)
                        .orElseThrow(() -> new RuntimeException("Roles not found"));
                LOG.info("Role {} has been found!", roles.getName());
            } catch(RuntimeException rte) {
                LOG.info("Role {} is not found, inserting new role to the database...", c.name());
                Role roles = new Role();
                roles.setName(c);
                roleRepository.save(roles);
            }
        }
    }
}
