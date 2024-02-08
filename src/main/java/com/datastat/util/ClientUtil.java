/* This project is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 PURPOSE.
 See the Mulan PSL v2 for more details.
 Create: 2024/02
*/

package com.datastat.util;

import java.util.Enumeration;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClientUtil.class);
    public static String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR", "X-Real-IP"};

        for (String headerName : headerNames) {
            String ip = request.getHeader(headerName);
            if (isValidIp(ip)) {
                return extractIp(ip);
            }
        }

        return request.getRemoteAddr();
    }

    private static boolean isValidIp(String ip) {
        return ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip);
    }

    private static String extractIp(String ip) {
        if (ip.contains(",")) {
            return ip.split(",")[0];
        }
        return ip;
    }

    public static void getHeaderValue(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            logger.info("request header: name = {}, value = {}", name, value);
        }
    }

}
