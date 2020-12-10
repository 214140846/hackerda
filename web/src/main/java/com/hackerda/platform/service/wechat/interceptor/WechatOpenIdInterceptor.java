package com.hackerda.platform.service.wechat.interceptor;

import com.hackerda.platform.builder.TextBuilder;
import com.hackerda.platform.domain.student.StudentRepository;
import com.hackerda.platform.domain.student.WechatStudentUserBO;
import com.hackerda.platform.domain.wechat.UnionId;
import com.hackerda.platform.domain.wechat.UnionIdRepository;
import com.hackerda.platform.domain.wechat.WechatUser;
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
		WechatStudentUserBO student;
		if (unionId.isEmpty()) {
			WxMpUser userInfo = getUserInfo(wxMpService, openid, wechatUser);

			boolean subscribe = BooleanUtils.toBoolean(userInfo.getSubscribe());
			context.put("subscribe", subscribe);
			if (!subscribe) {
				return false;
			}

			String userInfoUnionId = userInfo.getUnionId();
			UnionId existUnionId = unionIdRepository.find(userInfoUnionId);
			existUnionId.bindOpenid(wechatUser);
			unionIdRepository.save(existUnionId);

			student = studentRepository.findWetChatUser(existUnionId);
		} else {
			student = studentRepository.findWetChatUser(unionId);
		}

		context.put("student", student);

		return student != null;
	}

	private WxMpUser getUserInfo(WxMpService wxMpService, String openid, WechatUser wechatUser) {
		try {
			return wxMpService.getUserService().userInfo(openid);
		} catch (WxErrorException e) {
			log.error("{} get user info error", wechatUser, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
		if(!BooleanUtils.toBoolean((Boolean) context.get("subscribe"))) {
			return new TextBuilder().build("未关注公众号，无法使用此功能", wxMessage, wxMpService);
		}

		return new TextBuilder().build("请先点击下方菜单登录", wxMessage, wxMpService);
	}

}
