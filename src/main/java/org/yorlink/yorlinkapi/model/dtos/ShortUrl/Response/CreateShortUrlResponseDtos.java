package org.yorlink.yorlinkapi.model.dtos.ShortUrl.Response;

import lombok.Builder;
import lombok.Data;
import org.yorlink.yorlinkapi.model.entity.ShortUrlEntity;

import java.time.LocalDate;


@Data
@Builder
public class CreateShortUrlResponseDtos {
    private Long id;
    private String alias;
    private String originalUrl;
    private ShortUrlEntity.ShortUrlType shortUrlType;
    private LocalDate activationDate;
    private LocalDate expirationDate;
    private LocalDate createdAt;
}
