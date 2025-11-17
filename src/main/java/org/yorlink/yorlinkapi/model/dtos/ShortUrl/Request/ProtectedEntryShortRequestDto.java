package org.yorlink.yorlinkapi.model.dtos.ShortUrl.Request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProtectedEntryShortRequestDto {
    private Long id;
    private String password;
}
