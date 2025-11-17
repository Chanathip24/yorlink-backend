package org.yorlink.yorlinkapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yorlink.yorlinkapi.model.entity.ShortUrlEntity;

public interface ShortUrlRepository extends JpaRepository<ShortUrlEntity,Long> {
}
