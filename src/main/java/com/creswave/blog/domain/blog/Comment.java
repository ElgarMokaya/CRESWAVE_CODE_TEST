package com.creswave.blog.domain.blog;

import com.creswave.blog.domain.Domain;
import com.creswave.blog.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
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
@Table( name = "comment" )
public class Comment extends Domain {

    @Column( name = "text", nullable = false, unique = false, length = 2000 )
    private String text;

    @ManyToOne( )
    @JoinColumn( name = "fk_post",
            foreignKey = @ForeignKey( name = "comment_fk_post" ),
            nullable = false )
    private Post post;

    @ManyToOne( )
    @JoinColumn( name = "fk_user",
            foreignKey = @ForeignKey( name = "comment_fk_user" ),
            nullable = false )
    private User user;
}
