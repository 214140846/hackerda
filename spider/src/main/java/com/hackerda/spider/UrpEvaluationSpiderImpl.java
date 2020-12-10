package com.hackerda.spider;

import com.hackerda.spider.captcha.CaptchaImage;
import com.hackerda.spider.captcha.ICaptchaProvider;
import com.hackerda.spider.cookie.AccountCookiePersist;
import com.hackerda.spider.predict.CaptchaPredict;
import com.hackerda.spider.support.search.evaluation.EvaluationPagePost;
import com.hackerda.spider.support.search.evaluation.EvaluationPost;
import com.hackerda.spider.support.search.evaluation.searchresult.TeachingEvaluation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;

public class UrpEvaluationSpiderImpl extends UrpBaseSpider implements UrpEvaluationSpider{

    private final String Teaching_Evaluation = ROOT + "/student/teachingEvaluation/teachingEvaluation/search";
    /**
     * 问卷页面
     */
    private final String EVALUATION_PAGE = ROOT + "/student/teachingEvaluation/teachingEvaluation" +
            "/evaluationPage";

    private final String EVALUATION = ROOT + "/student/teachingEvaluation/teachingEvaluation/evaluation";

    public UrpEvaluationSpiderImpl(RestOperations client,
                                   CaptchaPredict captchaPredict,
                                   ICaptchaProvider<CaptchaImage> captchaProvider,
                                   AccountCookiePersist<String> cookiePersist,
                                   IExceptionHandler exceptionHandler) {
        super(client, captchaPredict, captchaProvider, cookiePersist, exceptionHandler);
    }

    @Override
    public TeachingEvaluation searchTeachingEvaluationInfo() {

        String content = getContent(Teaching_Evaluation);
        return parseObject(content, TeachingEvaluation.class);
    }

    @Override
    public String getEvaluationToken(EvaluationPagePost evaluationPagePost) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("evaluatedPeople", evaluationPagePost.getEvaluatedPeople());
        map.add("evaluatedPeopleNumber", evaluationPagePost.getEvaluatedPeopleNumber());
        map.add("questionnaireCode", evaluationPagePost.getQuestionnaireCode());
        map.add("questionnaireName", evaluationPagePost.getQuestionnaireName());
        map.add("evaluationContentNumber", evaluationPagePost.getEvaluationContentNumber());
        map.add("evaluationContentContent", evaluationPagePost.getEvaluationContentContent());


        ResponseEntity<String> entity = postFormData(map, EVALUATION_PAGE,  String.class);

        Document document = Jsoup.parse(entity.getBody());
        Element element = document.getElementById("tokenValue");

        return element.attr("value");

    }

    @Override
    public void evaluate(EvaluationPost evaluationPost) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("0000000050", evaluationPost.getFirst());
        map.add("0000000051", evaluationPost.getSecond());
        map.add("0000000052", evaluationPost.getThird());
        map.add("0000000053", evaluationPost.getFourth());
        map.add("zgpj", evaluationPost.getComment());
        map.add("questionnaireCode", evaluationPost.getQuestionnaireCode());
        map.add("evaluationContentNumber", evaluationPost.getEvaluationContentNumber());
        map.add("evaluatedPeopleNumber", evaluationPost.getEvaluatedPeopleNumber());
        map.add("tokenValue", evaluationPost.getTokenValue());
        map.add("count", evaluationPost.getCount());

        ResponseEntity<String> entity = postFormData(map, EVALUATION,  String.class);

    }
}
