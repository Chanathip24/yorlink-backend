package org.yorlink.yorlinkapi.model.dtos.ShortUrl.Response;

import lombok.Builder;
import lombok.Data;
import org.yorlink.yorlinkapi.model.entity.ShortUrlEntity;

import java.time.LocalDate;

@Data
@Builder
public class ShortUrlResponseDetailDtos {
    private Long id;
    private String alias;
    private ShortUrlEntity.ShortUrlType shortUrlType;
    private LocalDate createdAt;
    private Integer totalClicks;
    private Boolean isActive;
}
