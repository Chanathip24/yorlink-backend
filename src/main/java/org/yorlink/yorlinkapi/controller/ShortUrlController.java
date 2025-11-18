package org.yorlink.yorlinkapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yorlink.yorlinkapi.model.ApiResponse;
import org.yorlink.yorlinkapi.model.dtos.ShortUrl.Request.ProtectedEntryShortRequestDto;
import org.yorlink.yorlinkapi.model.dtos.ShortUrl.Request.ShortUrlRequestDtos;
import org.yorlink.yorlinkapi.model.dtos.ShortUrl.Response.CreateShortUrlResponseDtos;
import org.yorlink.yorlinkapi.model.dtos.ShortUrl.Response.ShortUrlResponseDetailDtos;
import org.yorlink.yorlinkapi.service.ClientService;
import org.yorlink.yorlinkapi.service.ShortUrlService;

@Controller
@RequestMapping("/api/public/url")
@RequiredArgsConstructor
public class ShortUrlController {

    private final ShortUrlService shortUrlService;
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateShortUrlResponseDtos>> saveShortUrl(@RequestBody ShortUrlRequestDtos request, HttpServletRequest httpRequest) {
        String clientIp = clientService.getClientIp(httpRequest);
        CreateShortUrlResponseDtos result = shortUrlService.createShortUrl(request,clientIp);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<ShortUrlResponseDetailDtos>> getShortUrlDetail(@RequestParam String alias){
        ShortUrlResponseDetailDtos result = shortUrlService.getShortUrlFromAlias(alias);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/protected")
    public ResponseEntity<ApiResponse<Object>> accessProtectedUrl(@RequestBody ProtectedEntryShortRequestDto request){
        shortUrlService.protectedUrlShortUrl(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteShortUrl(@PathVariable Long id){
        shortUrlService.deleteShortUrl(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
