package com.creswave.blog.domain.token;

import com.creswave.blog.domain.Domain;
import com.creswave.blog.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
public class Token extends Domain {

    @Column( unique = true )
    public String token;

    @Enumerated( EnumType.STRING )
    @Builder.Default
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;
    public boolean expired;

    @ManyToOne
    @JoinColumn( name = "user_id" )
    public User user;
}
