package org.yorlink.yorlinkapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yorlink.yorlinkapi.model.entity.ShortUrlEntity;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrlEntity,Long> {
    boolean existsByAlias(String alias);

    Optional<ShortUrlEntity> findByAlias(String alias);
    Optional<ShortUrlEntity> findByAliasAndOriginalUrl(String alias,String originalUrl);
    Optional<ShortUrlEntity> findByOriginalUrlAndTypeAndIpAddressAndIsCustomAlias(String originalUrl,ShortUrlEntity.ShortUrlType type,
                                                                                  String ipAddress,boolean isCustomAlias);
}
