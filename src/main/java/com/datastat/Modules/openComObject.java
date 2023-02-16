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

import org.springframework.stereotype.Repository;

import com.datastat.Modules.Provider.MindSpore;
import com.datastat.Modules.Provider.openEuler;
import com.datastat.Modules.helpers.Community;


@Repository
public class openComObject implements abstractCommunity{

    protected static String index;

    public void setBaseindex(String index) {
        openComObject.index = index;
    }

    /**
     * @return
     */
    public static String getBaseindex() {
        return index;
    }

    public CommunityData createPlayer(Community type) {
        switch (type) {
            case MINDSPORE:
                return new MindSpore(type);
            case OPENEULER:
                return new openEuler(type);

            default:
                throw new IllegalArgumentException("Invalid player type: " + type);
        }
    }
}
