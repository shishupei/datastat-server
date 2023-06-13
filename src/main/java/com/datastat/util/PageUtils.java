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

package com.datastat.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageUtils {
    private static final Logger logger = LoggerFactory.getLogger(PageUtils.class);
    public static Map getDataByPage(int currentPage, int pageSize, List data) {
        int dataSize = data.size();
        int totalPage = dataSize / pageSize + 1;

        HashMap<Object, Object> resultMap = new HashMap<>();
        int startIndex = multiplyExact(currentPage - 1, pageSize);
        int endIndex = multiplyExact(currentPage, pageSize);
        try {
            List list = currentPage >= totalPage ? data.subList(startIndex, dataSize) : data.subList(startIndex, endIndex);
            resultMap.put("data", list);
            resultMap.put("total", dataSize);
        } catch (Exception e) {
            logger.error("exception", e);
            resultMap.put("data", null);
            resultMap.put("total", dataSize);
        }
        return resultMap;
    }

    public static int multiplyExact(int a, int b) {
        long mul = (long) a * (long) b;
        if (mul > Integer.MAX_VALUE) {
            throw new ArithmeticException("Overflow!");
        }
        return  (int) mul;
    }

}
