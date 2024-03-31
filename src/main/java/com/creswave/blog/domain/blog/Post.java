package com.creswave.blog.domain.blog;

import java.util.List;

import com.creswave.blog.domain.Domain;
import com.creswave.blog.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
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
@Table( name = "post" )
public class Post extends Domain {

    @Column( name = "title", nullable = false, unique = true, length = 256 )
    private String title;

    @Column( name = "text", nullable = false, unique = false, length = 2000 )
    private String text;

    @ToString.Exclude
    @OneToMany( mappedBy = "post" )
    private List<Comment> comments;

    @ManyToOne( )
    @JoinColumn( name = "fk_user",
            foreignKey = @ForeignKey( name = "comment_fk_user" ),
            nullable = false )
    private User user;
}
