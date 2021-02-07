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
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthRoleServiceImpl implements AuthRoleService {
    private final AuthRoleRepository authRoleRepository;
    @Override
    public Set<String> getUserRoleLevels(AuthUser authUser) {
    return authRoleRepository.findByUserId(authUser.getId()).stream()
        .map(AuthRole::getLevel)
        .map(String::valueOf)
        .collect(Collectors.toSet());
    }
}
