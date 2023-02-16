/* This project is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 PURPOSE.
 See the Mulan PSL v2 for more details.
 Create: 2022
*/

package com.datastat.Modules;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;



@Repository
public class propertiesObj {
    static Properties properties = new Properties();

    String mindSporeConfMd5;


    static ScheduledExecutorService service = Executors
            .newSingleThreadScheduledExecutor();

    propertiesObj(ApplicationContext applicationContext) {

        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
            }
        }, 5, 15, TimeUnit.SECONDS);
    }
}
