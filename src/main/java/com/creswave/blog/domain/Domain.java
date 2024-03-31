package com.creswave.blog.domain;

import java.util.Calendar;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Elgar Mokaya - 30 Mar 2024
 */
@MappedSuperclass
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Domain {

    @Getter( AccessLevel.PUBLIC )
    @Id
    @SequenceGenerator( name = "mySeqGen", sequenceName = "blog_seq",
            initialValue = 10000,
            allocationSize = 100 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "mySeqGen" )
    @EqualsAndHashCode.Include
    private Long id;

    @Getter( AccessLevel.PUBLIC )
    @Temporal( TemporalType.TIMESTAMP )
    @CreationTimestamp
    @Column( name = "created_at", updatable = false, nullable = false,
            columnDefinition = "TIMESTAMP WITH TIME ZONE" )
    private Calendar createdAt;

    @Getter( AccessLevel.PUBLIC )
    @Temporal( TemporalType.TIMESTAMP )
    @UpdateTimestamp
    @Column( name = "updated_at", updatable = true, nullable = false,
            columnDefinition = "TIMESTAMP WITH TIME ZONE" )
    private Calendar updatedAt;
}
