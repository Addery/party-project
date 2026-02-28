package com.addery.back.repository;

import com.addery.back.entity.RegistrationWhitelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationWhitelistRepository extends JpaRepository<RegistrationWhitelist, Integer> {
    /**
     * 根据手机号和身份证号查询白名单记录
     * @param phone 手机号
     * @param idCard 身份证号
     * @return 白名单记录，如果不存在则返回Optional.empty()
     */
    Optional<RegistrationWhitelist> findByPhoneAndIdCard(String phone, String idCard);
}