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

package com.datastat.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TeamupApplyForm {
  @Size(max = 50, message = "the length can not exceed 50")
  @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
  private String scene;

  @Size(max = 50, message = "the length can not exceed 50")
  @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
  private String version;

  @Size(max = 50, message = "the length can not exceed 50")
  @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
  private String nodes;
  
  @Size(max = 50, message = "the length can not exceed 50")
  @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
  private String hardware;

  @Size(max = 500, message = "the length can not exceed 500")
  private String description;

  @Size(max = 50, message = "the length can not exceed 50")
  @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
  private String name;

  @Size(max = 50, message = "the length can not exceed 50")
  @Pattern(regexp = "^[^<>%&$]*$", message = "Text format error")
  private String company;

  @Size(max = 50, message = "the length can not exceed 50")
  @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email format error")
  private String email;

  @Pattern(regexp = "^(?:(?:\\+|00)86)?1[3-9]\\d{9}$", message = "Phone format error")
  private String phone;

}
