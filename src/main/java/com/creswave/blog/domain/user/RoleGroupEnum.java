package com.creswave.blog.domain.user;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public enum RoleGroupEnum {

    STANDARD_USER( 1l, "StandardUser" );

    private Long id;
    private String name;

    private RoleGroupEnum( Long id, String name ) {

        this.id = id;
        this.name = name;
    }


    public Long getId() {

        return id;
    }


    public String getName() {

        return name;
    }
}
