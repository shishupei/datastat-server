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

package com.datastat.model;

import lombok.Data;

import java.util.Objects;

@Data
public class UserTagInfo {
    // giteeId
    private String giteeId;
    // 活跃度
    private double activity;
    //评审意愿
    private double willingness;
    // 在该仓库的PR评论数占比（在该仓库的PR评论数/该仓库所有PR评论）
    private double commentPercentInThisRepo;
    // PR评论总数
    private int commentTotal;
    // 评论过相关PR的相关性
    private double correlation;
    // 繁忙程度
    private int busyness;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTagInfo that = (UserTagInfo) o;
        return giteeId.equals(that.giteeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(giteeId);
    }
}
