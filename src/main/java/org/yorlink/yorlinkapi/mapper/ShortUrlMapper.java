package org.yorlink.yorlinkapi.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yorlink.yorlinkapi.model.dtos.ShortUrl.Request.ShortUrlRequestDtos;
import org.yorlink.yorlinkapi.model.dtos.ShortUrl.Response.CreateShortUrlResponseDtos;
import org.yorlink.yorlinkapi.model.dtos.ShortUrl.Response.ShortUrlResponseDetailDtos;
import org.yorlink.yorlinkapi.model.entity.ShortUrlEntity;
import org.yorlink.yorlinkapi.service.ClientService;

import java.time.LocalDate;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ShortUrlMapper {
    private final PasswordEncoder passwordEncoder;


    public ShortUrlResponseDetailDtos shortUrlResponseDetailDtos(ShortUrlEntity shortUrlEntity){

        return ShortUrlResponseDetailDtos.builder()
                .shortUrlType(shortUrlEntity.getType())
                .originalUrl(shortUrlEntity.getOriginalUrl())
                .id(shortUrlEntity.getId())
                .alias(shortUrlEntity.getAlias())
                .totalClicks(shortUrlEntity.getCurrentClicks())
                .isActive(shortUrlEntity.isActive())
                .expirationDate(shortUrlEntity.getExpirationDate())
                .activationDate(shortUrlEntity.getActivationDate())
                .passwordHint(shortUrlEntity.getPasswordHint())
                .createdAt(shortUrlEntity.getCreatedAt())
                .build();
    }

    public CreateShortUrlResponseDtos toCreateShortUrlResponseDtos(ShortUrlEntity shortUrlEntity){
        return CreateShortUrlResponseDtos.builder()
                .id(shortUrlEntity.getId())
                .originalUrl(shortUrlEntity.getOriginalUrl())
                .shortUrlType(shortUrlEntity.getType())
                .activationDate(shortUrlEntity.getActivationDate())
                .expirationDate(shortUrlEntity.getExpirationDate())
                .alias(shortUrlEntity.getAlias())
                .createdAt(shortUrlEntity.getCreatedAt())
                .build();
    }

    public ShortUrlEntity toShortUrlEntity(ShortUrlRequestDtos request,String alias,String clientIp){
        ShortUrlEntity shortUrlEntity = ShortUrlEntity.builder()
                .alias(alias)
                .originalUrl(request.getUrl())
                .type(request.getType())
                .activationDate(request.getActivationDate())
                .expirationDate(request.getExpirationDate())
                .maximumClicks(request.getMaximumClicks())
                .currentClicks(0)
                .passwordHint(request.getPasswordHint())
                .isCustomAlias(request.getIsCustomAlias())
                .createdAt(LocalDate.now())
                .ipAddress(clientIp)
                .build();


        if(request.getType().equals(ShortUrlEntity.ShortUrlType.protect) && !request.getPassword().isEmpty()){
            String passwordHash = passwordEncoder.encode(request.getPassword());
            shortUrlEntity.setPasswordHash(passwordHash);
        }
        return shortUrlEntity;

    }
}
