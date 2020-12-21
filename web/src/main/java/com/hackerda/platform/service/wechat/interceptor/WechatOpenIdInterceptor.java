package com.hackerda.platform.service.wechat.interceptor;

import com.hackerda.platform.application.UnionIdApp;
import com.hackerda.platform.builder.TextBuilder;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
import com.hackerda.platform.service.wechat.UnSubscribeException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author junrong.chen
 * @date 2018/10/22
 */
@Service("wechatOpenIdInterceptor")
@Slf4j
public class WechatOpenIdInterceptor implements WxMessageInterceptor {

	@Autowired
	private UnionIdRepository unionIdRepository;
	@Autowired
	private StudentRepository studentRepository;


	@Override
	public boolean intercept(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
		String appId = wxMpService.getWxMpConfigStorage().getAppId();
		String openid = wxMessage.getFromUser();

		WechatUser wechatUser = new WechatUser(appId, openid);
		UnionId unionId = unionIdRepository.find(wechatUser);

		WechatStudentUserBO student = studentRepository.findWetChatUser(unionId);
		context.put("student", student);

		return student != null;
	}

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
		return new TextBuilder().build("请先点击下方菜单绑定", wxMessage, wxMpService);
	}

}
