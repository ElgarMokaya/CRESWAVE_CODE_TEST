package com.creswave.blog.domain.user;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
public enum RoleEnum {

    CREATE_COMMENT( 1l, "Create Comment" );

    private Long id;
    private String name;

    private RoleEnum( Long id, String name ) {

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
