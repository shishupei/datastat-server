/* This project is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 PURPOSE.
 See the Mulan PSL v2 for more details.
 Create: 2023
*/

package com.datastat.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.datastat.model.CustomPropertiesConfig;
import com.datastat.model.TokenUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


@Service
public class JwtTokenCreateService extends QueryService {
    public String getToken(TokenUser user) {
        CustomPropertiesConfig queryConf = getQueryConf(user.getCommunity());

        // 过期时间
        LocalDateTime nowDate = LocalDateTime.now();
        Date issuedAt = Date.from(nowDate.atZone(ZoneId.systemDefault()).toInstant());
        int expireSeconds = Integer.parseInt(queryConf.getTokenExpireSeconds());
        LocalDateTime expireDate = nowDate.plusSeconds(expireSeconds);
        Date expireAt = Date.from(expireDate.atZone(ZoneId.systemDefault()).toInstant());
        String basePassword = queryConf.getTokenBasePassword();

        return JWT.create()
                .withAudience(user.getUsername()) //谁接受签名
                .withIssuedAt(issuedAt) //生成签名的时间
                .withExpiresAt(expireAt) //过期时间
                .sign(Algorithm.HMAC256(user.getPassword() + basePassword));
    }
}
