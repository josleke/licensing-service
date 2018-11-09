package com.jade.microservices.book.ms.repository.impl;

import com.jade.microservices.book.ms.model.OrganizationDto;
import com.jade.microservices.book.ms.repository.OrganizationRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * @Author: Josiah Adetayo
 * @Email: j.adetayo@bcs.org.uk, josiah.adetayo@cwg-plc.com
 * @Date: 9/18/18
 */
@Repository
public class OrganizationRedisRepositoryImpl implements OrganizationRedisRepository {

    // The name of the hash in your Redis server where organization data is stored
    private static final String HASH_NAME="organization";

    // The HashOperations class is a set of Spring helper methods for carrying out
    // data operations on the Redis server
    private RedisTemplate<String, OrganizationDto> redisTemplate;
    private HashOperations hashOperations;

    public OrganizationRedisRepositoryImpl() { super(); }

    @Autowired
    public OrganizationRedisRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        hashOperations = redisTemplate.opsForHash(); // Operations for Hash
    }

    @Override
    public void saveOrganization(OrganizationDto organizationDto) {
        // All interactions with Redis will be with a single Organization object stored by its key.
        hashOperations.put(HASH_NAME, organizationDto.getOrganizationId(), organizationDto);
    }

    @Override
    public void updateOrganization(OrganizationDto organizationDto) {
        hashOperations.put(HASH_NAME, organizationDto.getOrganizationId(), organizationDto);
    }

    @Override
    public void deleteOrganization(String organizationId) {
        hashOperations.delete(HASH_NAME, organizationId);
    }

    @Override
    public OrganizationDto findOrganizationDto(String organizationId) {
        return (OrganizationDto) hashOperations.get(HASH_NAME, organizationId);
    }
}
