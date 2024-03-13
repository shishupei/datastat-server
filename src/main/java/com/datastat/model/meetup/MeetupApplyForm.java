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

package com.datastat.model.meetup;

import java.util.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MeetupApplyForm {
    @Size(max = 50, message = "the length can not exceed 50")
    @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
    private String company;

    @Size(max = 50, message = "the length can not exceed 50")
    @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
    private String topic;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date format error")
    private String date;

    @Valid
    private SurveyAnswer duration;

    @Size(max = 50, message = "the length can not exceed 50")
    @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
    private String city;

    @Size(max = 10, message = "the length can not exceed 10")
    @Pattern(regexp = "^[0-9\\s\\u4e00-\\u9fa5]+$", message = "Text format error")
    private String meetupSize;

    @Size(max = 50, message = "the length can not exceed 50")
    @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
    private String principalUser;

    @Size(max = 50, message = "the length can not exceed 50")
    @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
    private String principalCompany;

    @Pattern(regexp = "^(?:(?:\\+|00)86)?1[3-9]\\d{9}$", message = "Phone format error")
    private String principalPhone;

    @Size(max = 50, message = "the length can not exceed 50")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email format error")
    private String principalEmail;

    @Valid
    private SurveyAnswer meetupFormat;

    @Valid
    private ArrayList<SurveyAnswer> supports;

    @Size(max = 500, message = "the length can not exceed 500")
    private String details;

    public ArrayList<String> validMeetupApplyFormField() {
        ArrayList<String> errorMesseges = new ArrayList<>();
        if (!validDuration(getDuration())) {
            errorMesseges.add("duration error");
        }
        if (!validMeetupFormat(getMeetupFormat())) {
            errorMesseges.add("meetup format error");
        }
        return errorMesseges;
    }

    public Boolean validDuration(SurveyAnswer value){
        List<String> valueList = Arrays.asList("半天", "全天", "其他");
        String duration = value.getOptional();
        if (valueList.contains(duration)) {
            return true;
        }
        return false;
    }

    public Boolean validMeetupFormat(SurveyAnswer value){
        List<String> valueList = Arrays.asList("线上活动", "线下活动", "线上+线下", "其他");
        String format = value.getOptional();
        if (valueList.contains(format)) {
            return true;
        }
        return false;
    }

}
