package org.yorlink.yorlinkapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "short_urls")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ShortUrlEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // maps SERIAL
    private Long id;

    @Column(unique = true, nullable = false)
    private String alias;

    @Column(nullable = false,unique = true, columnDefinition = "TEXT")
    private String originalUrl;


    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private ShortUrlType type = ShortUrlType.normal; // normal, scheduled, expiring, protect

    public enum ShortUrlType{
        normal,scheduled,expiring,protect
    }

    @Column(name = "activation_date")
    private LocalDate activationDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "maximum_clicks")
    private Integer maximumClicks;

    @Column(name = "current_clicks")
    private Integer currentClicks = 0;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "password_hint")
    private String passwordHint;

    private Boolean isCustomAlias = false;

    private LocalDate createdAt = LocalDate.now();
    private LocalDate updatedAt = LocalDate.now();

    public boolean isActive() {
        LocalDate today = LocalDate.now();

        switch (type) {
            case ShortUrlType.normal:
            case ShortUrlType.protect:
                return true; // always active

            case ShortUrlType.scheduled:
                return activationDate != null && !today.isBefore(activationDate);

            case ShortUrlType.expiring:
                boolean notExpiredByDate = expirationDate == null || !today.isAfter(expirationDate);
                boolean notExpiredByClicks = maximumClicks == null || currentClicks < maximumClicks;
                return notExpiredByDate && notExpiredByClicks;

            default:
                return false;
        }
    }
}
