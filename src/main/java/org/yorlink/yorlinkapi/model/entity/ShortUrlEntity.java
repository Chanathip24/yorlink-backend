package org.yorlink.yorlinkapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "short_urls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShortUrlEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // maps SERIAL
    private Long id;

    @Column(unique = true, nullable = false)
    private String alias;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    @Column(nullable = false)
    private String type; // normal, scheduled, expiring, protected

    private LocalDate activationDate;
    private LocalDate expirationDate;

    private Integer maximumClicks;
    private Integer currentClicks = 0;

    private String passwordHash;
    private String passwordHint;

    private Boolean isCustomAlias = false;

    private LocalDate createdAt = LocalDate.now();
    private LocalDate updatedAt = LocalDate.now();
}
