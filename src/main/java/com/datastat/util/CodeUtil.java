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

package com.datastat.util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Component
public class CodeUtil {
    private static final Logger logger =  LoggerFactory.getLogger(CodeUtil.class);

    // 华为云MSGSMS，用于格式化鉴权头域，给"Authorization"参数赋值
    public static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    // 华为云MSGSMS，用于格式化鉴权头域，给"X-WSSE"参数赋值
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";

    public static String sendCode(String accountType, String account, Environment env) {
        String resMsg = "fail";
        try {
            switch (accountType.toLowerCase()) {
                case "phone":
                    // 短信发送服务器
                    String msgsms_app_key = env.getProperty("msgsms.app_key");
                    String msgsms_app_secret = env.getProperty("msgsms.app_secret");
                    String msgsms_url = env.getProperty("msgsms.url");
                    String msgsms_signature = env.getProperty("msgsms.signature");
                    String msgsms_sender = env.getProperty("msgsms.sender");
                    String msgsms_template_id = env.getProperty("msgsms.template.id");
                    // 模板无变量
                    String templateParas = "";
                    String wsseHeader = buildWsseHeader(msgsms_app_key, msgsms_app_secret);
                    String body = buildSmsBody(msgsms_sender, account, msgsms_template_id,
                            templateParas, "", msgsms_signature);
                    // 发送短信
                    HttpResponse<JsonNode> response = Unirest.post(msgsms_url)
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .header("Authorization", CodeUtil.AUTH_HEADER_VALUE)
                            .header("X-WSSE", wsseHeader)
                            .body(body)
                            .asJson();
                    
                    if (response.getStatus() == 200) resMsg = "success";
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("Sendcode Error", e.getMessage());
        }
        
        return resMsg;
    }

    /**
     * 短信发送请求body
     *
     * @param sender         签名通道号(发送方)
     * @param receiver       接受号码，号码格式(包含国家码),示例:+8615123456789,多个号码之间用英文逗号分隔
     * @param templateId     模板ID
     * @param templateParas  模板内容
     * @param statusCallBack 选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
     * @param signature      签名名称
     * @return
     */
    public static String buildSmsBody(String sender, String receiver, String templateId, String templateParas,
                               String statusCallBack, String signature) {
        if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty()
                || templateId.isEmpty()) {
            logger.error("buildRequestBody(): sender, receiver or templateId is null.");
            return null;
        }
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("from", sender);
        map.put("to", receiver);
        map.put("templateId", templateId);
        if (null != templateParas && !templateParas.isEmpty()) {
            map.put("templateParas", templateParas);
        }
        if (null != statusCallBack && !statusCallBack.isEmpty()) {
            map.put("statusCallback", statusCallBack);
        }
        if (null != signature && !signature.isEmpty()) {
            map.put("signature", signature);
        }
        System.out.println(map);

        StringBuilder sb = new StringBuilder();
        String temp = "";

        for (String s : map.keySet()) {
            try {
                temp = URLEncoder.encode(map.get(s), "UTF-8");
            } catch (Exception e) {
                logger.error("BuildSmsBody Error", e.getMessage());
            }
            sb.append(s).append("=").append(temp).append("&");
        }

        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * 短信发送请求header
     *
     * @param appKey    APP_Key
     * @param appSecret APP_Secret
     * @return
     */
    public static String buildWsseHeader(String appKey, String appSecret) throws NoSuchAlgorithmException {
        if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
            logger.error("buildWsseHeader(): appKey or appSecret is null.");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date());
        String nonce = randomStrBuilder(6);

        MessageDigest md;
        byte[] passwordDigest = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update((nonce + time + appSecret).getBytes());
            passwordDigest = md.digest();
        } catch (Exception e) {
            logger.error("BuildWsseHeader Error", e.getMessage());
        }

        // PasswordDigest
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(passwordDigest);

        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }

    /**
     * 随机生成验证码
     *
     * @return 验证码
     */
    public static String randomNumBuilder(int codeLength) throws NoSuchAlgorithmException {
        StringBuilder result = new StringBuilder();
        SecureRandom instance = SecureRandom.getInstanceStrong();
        for (int i = 0; i < codeLength; i++) {
            result.append(instance.nextInt(9));
        }
        return result.toString();
    }

    /**
     * 随机生成字符串
     *
     * @param strLength 字符串长度
     * @return 随机字符串
     */
    public static String randomStrBuilder(int strLength) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstanceStrong();
        return new BigInteger(160, random).toString(strLength);
    }

}
