package org.yorlink.yorlinkapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.yorlink.yorlinkapi.mapper.ShortUrlMapper;
import org.yorlink.yorlinkapi.model.dtos.ShortUrl.Request.ProtectedEntryShortRequestDto;
import org.yorlink.yorlinkapi.model.dtos.ShortUrl.Request.ShortUrlRequestDtos;
import org.yorlink.yorlinkapi.model.dtos.ShortUrl.Response.CreateShortUrlResponseDtos;
import org.yorlink.yorlinkapi.model.dtos.ShortUrl.Response.ShortUrlResponseDetailDtos;
import org.yorlink.yorlinkapi.model.entity.ShortUrlEntity;
import org.yorlink.yorlinkapi.repository.ShortUrlRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlMapper shortUrlMapper;

    private static final String ALIAS_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ALIAS_LENGTH = 6;
    private final BCryptPasswordEncoder passwordEncoder;

    public String generateUniqueAlias(String requestedAlias) {
        if (requestedAlias != null && !requestedAlias.isBlank()) {
            // Custom alias provided
            if (shortUrlRepository.existsByAlias(requestedAlias)) {
                throw new RuntimeException("Alias already in use!");
            }
            return requestedAlias;
        }

        // Generate random alias until unique
        String alias;
        do {
            alias = generateRandomAlias(ALIAS_LENGTH);
        } while (shortUrlRepository.existsByAlias(alias));

        return alias;
    }

    private String generateRandomAlias(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int idx = (int) (Math.random() * ALIAS_CHARS.length());
            sb.append(ALIAS_CHARS.charAt(idx));
        }
        return sb.toString();
    }

    public CreateShortUrlResponseDtos createShortUrl(ShortUrlRequestDtos request,String clientIp) {
        //find original link first
        if(request.getIsCustomAlias() != null && request.getIsCustomAlias() && request.getAlias() != null && !request.getAlias().isEmpty()) {
            Optional<ShortUrlEntity> exist = shortUrlRepository.findByAliasAndOriginalUrl(request.getAlias(),request.getUrl());
            if(exist.isPresent()) {
                return shortUrlMapper.toCreateShortUrlResponseDtos(exist.get());
            }
        }
        else{
            Optional<ShortUrlEntity> existUrl = shortUrlRepository.findByOriginalUrlAndTypeAndIpAddressAndIsCustomAlias(request.getUrl()
                    ,request.getType(),clientIp,false);
            if(existUrl.isPresent()){
                return shortUrlMapper.toCreateShortUrlResponseDtos(existUrl.get());
            }
        }


        String genAlias;

        if (request.getIsCustomAlias() != null && request.getIsCustomAlias()) {
            genAlias = request.getAlias();
        } else {
            genAlias = generateRandomAlias(6);
        }

        ShortUrlEntity shortUrlEntity = shortUrlMapper.toShortUrlEntity(request,genAlias,clientIp);

        ShortUrlEntity savedUrl = shortUrlRepository.save(shortUrlEntity);

        return shortUrlMapper.toCreateShortUrlResponseDtos(savedUrl);
    }

    public void protectedUrlShortUrl(ProtectedEntryShortRequestDto request){
        ShortUrlEntity shortUrlEntity = shortUrlRepository.findById(request.getId()).orElseThrow(()-> new RuntimeException("No such shortUrl"));

        String shortUrlPassword = shortUrlEntity.getPasswordHash();
        String requestPassword = request.getPassword();

        boolean matches = passwordEncoder.matches(requestPassword, shortUrlPassword);
        if(!matches) {
            throw new RuntimeException("Password does not match!");
        }

    }

    //get detail
    public ShortUrlResponseDetailDtos getShortUrl(Long shortUrlId){
        ShortUrlEntity shortUrlEntity = shortUrlRepository.findById(shortUrlId).orElseThrow(()-> new RuntimeException("No such shortUrl"));
        return shortUrlMapper.shortUrlResponseDetailDtos(shortUrlEntity);
    }

    //delete
    public void deleteShortUrl(Long shortUrlId){
        shortUrlRepository.findById(shortUrlId).orElseThrow(()-> new RuntimeException("No such shortUrl to delete"));
        shortUrlRepository.deleteById(shortUrlId);
    }
    public ShortUrlResponseDetailDtos getShortUrlFromAlias(String alias){
        ShortUrlEntity shortUrlEntity = shortUrlRepository.findByAlias(alias).orElseThrow(()-> new RuntimeException("No such shortUrl"));
        return shortUrlMapper.shortUrlResponseDetailDtos(shortUrlEntity);
    }
}
