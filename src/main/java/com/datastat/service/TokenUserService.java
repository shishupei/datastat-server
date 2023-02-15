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

import com.datastat.model.CustomPropertiesConfig;
import com.datastat.model.TokenUser;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class TokenUserService extends QueryService {
    @SneakyThrows
    public TokenUser findByUsername(String community, String name) {
        if (name == null) return null;

        CustomPropertiesConfig queryConf = getQueryConf(community);
        String userName = queryConf.getTokenUserName();
        if (name.equals(userName)) {
            String password = queryConf.getTokenUserPassword();
            return new TokenUser(community, userName, password);
        }
        return null;
    }
}
