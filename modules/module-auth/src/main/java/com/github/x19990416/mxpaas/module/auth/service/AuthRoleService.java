/**
 * create by Guo Limin on 2021/2/6.
 */
package com.github.x19990416.mxpaas.module.auth.service;

import com.github.x19990416.mxpaas.module.auth.domain.AuthRole;
import com.github.x19990416.mxpaas.module.auth.domain.AuthUser;

import java.util.List;

public interface AuthRoleService {
    public List<AuthRole> getUserRoles(AuthUser authUser);
}
