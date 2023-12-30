package com.mmd.library.Repository.authentication;

import com.mmd.library.entity.authentication.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, String> {
}
