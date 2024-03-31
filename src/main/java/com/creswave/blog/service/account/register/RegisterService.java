package com.creswave.blog.service.account.register;

import com.creswave.blog.domain.user.RoleGroupEnum;
import com.creswave.blog.service.account.register.email.RegisterRequest;
import com.creswave.blog.service.account.register.email.RegisterResponse;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface RegisterService {


    RegisterResponse register( RegisterRequest request,
            RoleGroupEnum roleGroupEnum );
}
