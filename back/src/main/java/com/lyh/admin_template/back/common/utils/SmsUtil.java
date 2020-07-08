package com.lyh.admin_template.back.common.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.lyh.admin_template.back.modules.sms.entity.SmsResponse;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * sms 短信发送工具类
 */
@Data
@Component
public class SmsUtil {
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.signName}")
    private String signName;
    @Value("${aliyun.templateCode}")
    private String templateCode;
    @Value("${aliyun.regionId}")
    private String regionId;
    private final static String OK = "OK";

    /**
     * 发送短信
     */
    public String sendSms(String phoneNumbers) {
        if (StringUtils.isEmpty(phoneNumbers)) {
            return null;
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        // 固定参数，无需修改
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", regionId);

        // 设置手机号
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        // 设置签名模板
        request.putQueryParameter("SignName", signName);
        // 设置短信模板
        request.putQueryParameter("TemplateCode", templateCode);
        // 设置短信验证码
        String code = getCode();
        request.putQueryParameter("TemplateParam", "{\"code\":" + code +"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            // 转换返回的数据（需引入 Gson 依赖）
            SmsResponse smsResponse = GsonUtil.fromJson(response.getData(), SmsResponse.class);
            // 当 message 与 code 均为 ok 时，短信发送成功、否则失败
            if (SmsUtil.OK.equals(smsResponse.getMessage()) && SmsUtil.OK.equals(smsResponse.getCode())) {
                return code;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取 6 位验证码
     */
    public String getCode() {
        return String.valueOf((int)((Math.random()*9+1)*100000));
    }
}
