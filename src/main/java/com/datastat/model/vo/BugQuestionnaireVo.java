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

import lombok.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class BugQuestionnaireVo {

    private String bugDocFragment;
    private ArrayList<String> existProblem = new ArrayList<>();
    private String problemDetail;
    private Integer comprehensiveSatisfication;

    @Pattern(regexp = "^[a-z0-9A-Z]+[-|a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$", message = "email format is invalid")
    @Size(max = 50)
    private String email;
    
    public ArrayList<String> checkoutFieldValidate(BugQuestionnaireVo bugQuestionnaireVo, String community, String lang) {
        List<String> existProblemTemplate;
        if ("openeuler".equalsIgnoreCase(community)) {
            if (lang.equals("en")) {
                existProblemTemplate = Arrays.asList("Specifications and Common Mistakes", "Correctness",
                        "Risk Warnings", "Usability", "Content Compliance");
            } else {
                existProblemTemplate = Arrays.asList("规范和低错类", "易用性", "正确性", "风险提示", "内容合规");
            }
        } else if ("openlookeng".equalsIgnoreCase(community)) {
            existProblemTemplate = Arrays.asList("文档存在风险与错误", "内容描述不清晰", "内容获取有困难", "示例代码错误", "内容有缺失");
        } else if ("mindspore".equalsIgnoreCase(community)) {
            existProblemTemplate = Arrays.asList("低错类", "易用性", "正确性", "风险提示", "内容合规");
        } else {
            return null;
        }
        
        List<Integer> comprehensiveSatisficationTemplate = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        String bugDocFragment = bugQuestionnaireVo.getBugDocFragment();
        ArrayList<String> existProblem = bugQuestionnaireVo.getExistProblem();
        String problemDetail = bugQuestionnaireVo.getProblemDetail();
        Integer comprehensiveSatisfication = bugQuestionnaireVo.getComprehensiveSatisfication();

        boolean existProblemValidation = existProblemTemplate.containsAll(existProblem);
        boolean comprehensiveSatisficationValidation = comprehensiveSatisficationTemplate
                .contains(comprehensiveSatisfication);

        if (bugDocFragment != null && bugDocFragment.contains("\\")) {
            String cleanBugDocFragment = bugDocFragment.replace("\\", "/");
            bugQuestionnaireVo.setBugDocFragment(cleanBugDocFragment);
        }
        if (problemDetail != null && problemDetail.contains("\\")) {
            String cleanProblemDetail = problemDetail.replace("\\", "/");
            bugQuestionnaireVo.setBugDocFragment(cleanProblemDetail);
        }

        ArrayList<String> errorMesseges = new ArrayList<>();

        if (!existProblemValidation) {
            errorMesseges.add("existProblem validate failure");
        }
        if (!comprehensiveSatisficationValidation) {
            errorMesseges.add("comprehensiveSatisfication validate failure");
        }

        return errorMesseges;
    }
}
