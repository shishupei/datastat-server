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


package com.datastat.controller;


import com.datastat.model.CustomPropertiesConfig;
import com.datastat.model.TokenUser;
import com.datastat.service.JwtTokenCreateService;
import com.datastat.service.QueryService;
import com.datastat.service.TokenUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(value = "/token")
@RestController
public class TokenController {
    @Autowired
    TokenUserService tokenUserService;
    @Autowired
    JwtTokenCreateService jwtTokenCreateService;

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public String apply(HttpServletRequest request,
                        @RequestBody TokenUser user) {

        HashMap<String, Object> jsonObject = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        TokenUser userForBase = tokenUserService.findByUsername(user.getCommunity(), user.getUsername());

        byte[] passwordBytes;
        try {
            passwordBytes = Base64.getDecoder().decode(user.getPassword());
        } catch (Exception ex) {
            jsonObject.put("code", 403);
            jsonObject.put("msg", "user name or password error");
            return objectMapper.valueToTree(jsonObject).toString();
        }

        if (userForBase == null || !userForBase.getPassword().equals(new String(passwordBytes))) {
            jsonObject.put("code", 403);
            jsonObject.put("msg", "user name or password error");
        } else {
            String token = jwtTokenCreateService.getToken(userForBase);
            jsonObject.put("code", 200);
            jsonObject.put("token", token);
            jsonObject.put("msg", "success");
        }

        return objectMapper.valueToTree(jsonObject).toString();
    }
}
