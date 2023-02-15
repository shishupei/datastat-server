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

package com.datastat.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class BuildCheckInfoQueryVo {
    @JsonProperty(value = "community_name")
    private String communityName;

    @JsonProperty(value = "pr_url")
    private String prUrl;

    @JsonProperty(value = "pr_title")
    private String prTitle;

    @JsonProperty(value = "pr_committer")
    private String prCommitter;

    @JsonProperty(value = "pr_branch")
    private String prBranch;

    @JsonProperty(value = "build_no")
    private String buildNo;

    @JsonProperty(value = "check_total")
    private String checkTotal;

    @JsonProperty(value = "build_duration")
    private Map<String, String> buildDuration;

    @JsonProperty(value = "pr_create_time")
    private Map<String, String> prCreateTime;

    @JsonProperty(value = "result_update_time")
    private Map<String, String> resultUpdateTime;

    @JsonProperty(value = "result_build_time")
    private Map<String, String> resultBuildTime;

    @JsonProperty(value = "mistake_update_time")
    private Map<String, String> mistakeUpdateTime;
}
