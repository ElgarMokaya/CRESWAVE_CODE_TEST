package com.creswave.blog.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creswave.blog.domain.user.RoleGroup;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public interface RoleGroupRepository extends JpaRepository<RoleGroup, Long> {

}
