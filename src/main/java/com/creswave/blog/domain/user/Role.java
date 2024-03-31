package com.creswave.blog.domain.user;

import com.creswave.blog.domain.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table( name = "role" )
public class Role extends Domain {

    @Column( name = "name", nullable = false, unique = true, length = 60 )
    private String name;

    @Column( name = "description", nullable = false, unique = false, length = 120 )
    private String description;
}
