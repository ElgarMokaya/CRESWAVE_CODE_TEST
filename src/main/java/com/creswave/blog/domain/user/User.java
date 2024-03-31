package com.creswave.blog.domain.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.creswave.blog.domain.Domain;
import com.creswave.blog.domain.token.Token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@Data
@EqualsAndHashCode( callSuper = true, onlyExplicitlyIncluded = true )
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "_user" )
public class User extends Domain {

    @Column( name = "active", nullable = false )
    private Boolean active;

    @Column( name = "first_name", nullable = true, unique = false, length = 256 )
    private String firstName;

    @Column( name = "last_name", nullable = true, unique = false, length = 256 )
    private String lastName;

    @Column( name = "email", nullable = true, unique = true, length = 255 )
    private String email;

    @Column( name = "username", nullable = false, unique = false, length = 256 )
    private String username;

    @Column( name = "avatarIdentifier", nullable = true, unique = false, length = 1024 )
    private String avatarIdentifier;

    @Column( name = "password_hash", nullable = true, unique = false, length = 256 )
    private String passwordHash;

    @Column( name = "email_activation_code", nullable = true, unique = false, length = 256 )
    private String emailActivationCode;

    @Column( name = "forgotten_password_code", nullable = true, unique = false, length = 256 )
    private String forgottenPasswordCode;

    @Builder.Default
    @ToString.Exclude
    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "user_role", joinColumns = @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey( name = "user_role_fk_user" ) ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    foreignKey = @ForeignKey( name = "user_role_fk_role" ),
                    referencedColumnName = "id" ) )
    private Set<Role> roles = new HashSet<>();

    @ToString.Exclude
    @OneToMany( mappedBy = "user" )
    private List<Token> tokens;
}
