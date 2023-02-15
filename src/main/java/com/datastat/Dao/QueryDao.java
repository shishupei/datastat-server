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

package com.datastat.Dao;

import org.springframework.stereotype.Repository;

import com.datastat.Modules.CommunityData;
import com.datastat.Modules.openComObject;
import com.datastat.Modules.helpers.Community;


@Repository
public class QueryDao {

    public String queryContributors(String community) {
        openComObject openobj = new openComObject();
        CommunityData communityObj = openobj.createPlayer(Community.MINDSPORE);
        int downloadAccount = communityObj.getDownloadAccount();
        System.out.println("Snooker player endurance: " + downloadAccount);
        return "{\"code\":" + 500 + ",\"data\":{\"" + community + "\":" + downloadAccount + "},\"msg\":\"" + "\"}";
    }
}
