/* This project is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 PURPOSE.
 See the Mulan PSL v2 for more details.
 Create: 2024
*/

package com.datastat.model;

import java.util.*;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SigGathering {
    @Size(max = 20, message = "the length can not exceed 20")
    @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
    private String name;

    @Size(max = 50, message = "the length can not exceed 50")
    @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
    private String userId;

    @Pattern(regexp = "^(?:(?:\\+|00)86)?1[3-9]\\d{9}$", message = "Phone format error")
    private String phone;

    @Size(max = 50, message = "the length can not exceed 50")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email format error")
    private String email;

    @Size(max = 20, message = "the length can not exceed 20")
    @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
    private String company;

    @Size(max = 20, message = "the length can not exceed 20")
    private List<String> sigs;

    @Size(max = 20, message = "the length can not exceed 20")
    private List<String> technicalSeminars;

    @Pattern(regexp = "^(agree|refuse)$", message = "value format error")
    private String attend;

    @Size(max = 20, message = "the length can not exceed 20")
    @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
    private String privacyVersion;

    @Pattern(regexp = "^(agree|refuse)$", message = "value format error")
    private String acceptPrivacyVersion;

    @Size(max = 1000, message = "the length can not exceed 1000")
    @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
    private String others;

    public Boolean validSeminars(List<String> technicalSeminars, String templates){
        List<String>valueList = Arrays.asList(templates.split(","));
        for (String technicalSeminar : technicalSeminars) {
            if (!valueList.contains(technicalSeminar)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<String> validField(String templates) {
        ArrayList<String> errorMesseges = new ArrayList<>();
        if (!validSeminars(getTechnicalSeminars(), templates)) {
            errorMesseges.add("technical seminars error");
        }
        return errorMesseges;
    }

}
