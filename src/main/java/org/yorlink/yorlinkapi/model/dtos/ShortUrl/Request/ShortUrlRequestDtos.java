package org.yorlink.yorlinkapi.model.dtos.ShortUrl.Request;

import lombok.Builder;
import lombok.Data;
import org.yorlink.yorlinkapi.model.entity.ShortUrlEntity;

import java.time.LocalDate;

@Data
@Builder
public class ShortUrlRequestDtos {
    private String url;
    private ShortUrlEntity.ShortUrlType type;
    private LocalDate activationDate;
    private LocalDate expirationDate;
    private Integer maximumClicks;
    private String password;
    private String passwordHint;
    private Boolean isCustomAlias;
    private String alias;
}
