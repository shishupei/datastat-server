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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import org.apache.commons.lang3.StringUtils;

public class StringValidationUtil {

    public static boolean isDateTimeStrValid(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return true;

        String format = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter ldt = DateTimeFormatter.ofPattern(format.replace("y", "u")).withResolverStyle(ResolverStyle.STRICT);
        try {
            return LocalDate.parse(dateStr, ldt) != null;
        } catch (Exception e) {
            return false;
        }
    }


}
