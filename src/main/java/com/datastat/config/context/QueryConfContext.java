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

package com.datastat.config.context;

import com.datastat.config.QueryConfig;
import com.datastat.model.CustomPropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class QueryConfContext {
    @Autowired
    private Map<String, CustomPropertiesConfig> queryConfMap;

    @Autowired
    QueryConfig queryConfig;

    public CustomPropertiesConfig getQueryConfig(String type) {
        return queryConfMap.getOrDefault(type, queryConfig);
    }
}
