/**
 * create by Guo Limin on 2021/2/6.
 */
package com.github.x19990416.mxpaas.module.auth.service.impl;

import com.github.x19990416.mxpaas.module.auth.domain.AuthRole;
import com.github.x19990416.mxpaas.module.auth.domain.AuthUser;
import com.github.x19990416.mxpaas.module.auth.repository.AuthRoleRepository;
import com.github.x19990416.mxpaas.module.auth.service.AuthRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class AuthRoleServiceImpl implements AuthRoleService {
    private final AuthRoleRepository authRoleRepository;
    @Override
    public List<AuthRole> getUserRoles(AuthUser authUser) {
        return authRoleRepository.findByUserId(authUser.getId());
    }
}
