package com.creswave.blog.domain.user;

import java.util.HashSet;
import java.util.Set;

import com.creswave.blog.domain.Domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table( name = "role_group" )
public class RoleGroup extends Domain {

    @Column( name = "name", nullable = false, unique = false, length = 60 )
    private String name;

    @Column( name = "description", nullable = true, unique = false,
            length = 120 )
    private String description;

    @Builder.Default
    @ToString.Exclude
    @ManyToMany( fetch = FetchType.EAGER, targetEntity = Role.class, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE } )
    @JoinTable( name = "rolegroup_role", joinColumns = { @JoinColumn(
            name = "rolegroup_id",
            foreignKey = @ForeignKey( name = "rolegroup_role_fk_rolegroup" ),
            referencedColumnName = "id" ) },
            inverseJoinColumns = { @JoinColumn(
                    name = "role_id",
                    foreignKey = @ForeignKey( name = "rolegroup_role_fk_role" ),
                    referencedColumnName = "id" ) } )
    private Set<Role> roles = new HashSet<Role>();
}
