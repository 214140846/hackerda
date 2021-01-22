package com.hackerda.platform.service;

import com.hackerda.platform.domain.constant.RedisKeys;
import com.hackerda.platform.domain.student.StudentAccount;
import com.hackerda.platform.domain.student.StudentUserBO;
import com.hackerda.platform.domain.time.SchoolTimeManager;
import com.hackerda.platform.domain.time.Term;
import com.hackerda.spider.UrpEvaluationSpider;
import com.hackerda.spider.support.search.evaluation.EvaluationPagePost;
import com.hackerda.spider.support.search.evaluation.EvaluationPost;
import com.hackerda.spider.support.search.evaluation.searchresult.TeachingEvaluation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EvaluationService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SchoolTimeManager schoolTimeManager;

    public boolean hasFinish(StudentAccount studentAccount) {
        Term term = getTerm();
        String key = RedisKeys.FINISH_EVALUATION_SET.genKey(term.asKey());
        return BooleanUtils.toBoolean(stringRedisTemplate.opsForSet().isMember(key, studentAccount.getAccount()));
    }

    public void addFinish(StudentAccount studentAccount) {
        Term term = getTerm();
        String key = RedisKeys.FINISH_EVALUATION_SET.genKey(term.asKey());
        stringRedisTemplate.opsForSet().add(key, studentAccount.getAccount());
    }

    public void push(StudentAccount studentAccount) {
        Term term = getTerm();
        String key = RedisKeys.WAITING_EVALUATION_LIST.genKey(term.asKey());
        stringRedisTemplate.opsForList().rightPush(key, studentAccount.getAccount());
    }

    public StudentAccount pop() {
        Term term = getTerm();
        String key = RedisKeys.WAITING_EVALUATION_LIST.genKey(term.asKey());
        String pop = stringRedisTemplate.opsForList().leftPop(key, 7, TimeUnit.DAYS);

        while (hasFinish(new StudentAccount(pop))) {
            pop = stringRedisTemplate.opsForList().leftPop(key, 7, TimeUnit.DAYS);
        }

        return new StudentAccount(pop);
    }

    public void evaluate(StudentUserBO wechatStudentUserBO) {
        UrpEvaluationSpider evaluationSpider = getUrpEvaluationSpider();
        evaluationSpider.login(wechatStudentUserBO.getAccount().toString(), wechatStudentUserBO.getEnablePassword());
        List<EvaluationPagePost> needToEvaluate = getEvaluationPagePost(evaluationSpider);

        for (EvaluationPagePost pagePost : needToEvaluate) {
            evaluate(pagePost, evaluationSpider);
        }

    }

    public boolean hasFinish(StudentUserBO wechatStudentUserBO) {
        UrpEvaluationSpider evaluationSpider = getUrpEvaluationSpider();
        evaluationSpider.login(wechatStudentUserBO.getAccount().toString(), wechatStudentUserBO.getEnablePassword());
        return getEvaluationPagePost(evaluationSpider).size() == 0;
    }

    public boolean hasTask() {
        Term term = getTerm();
        String key = RedisKeys.WAITING_EVALUATION_LIST.genKey(term.asKey());
        return Optional.ofNullable(stringRedisTemplate.opsForList().size(key)).orElse(0L) != 0;
    }

    private List<EvaluationPagePost> getEvaluationPagePost(UrpEvaluationSpider evaluationSpider) {

        TeachingEvaluation teachingEvaluation = evaluationSpider.searchTeachingEvaluationInfo();
        return teachingEvaluation.getData().stream()
                .filter(x -> "否".equals(x.getIsEvaluated()))
                .map(x -> new EvaluationPagePost()
                        .setQuestionnaireCode(x.getQuestionnaire().getQuestionnaireNumber())
                        .setQuestionnaireName(x.getQuestionnaire().getQuestionnaireName())
                        .setEvaluationContentNumber(x.getId().getEvaluationContentNumber())
                        .setEvaluatedPeople(x.getEvaluatedPeople())
                        .setEvaluatedPeopleNumber(x.getId().getEvaluatedPeople())
                ).collect(Collectors.toList());
    }


    private void evaluate(EvaluationPagePost pagePost, UrpEvaluationSpider evaluationSpider) {

        String token = evaluationSpider.getEvaluationToken(pagePost);
        EvaluationPost post = new EvaluationPost()
                .setTokenValue(token)
                .setEvaluatedPeopleNumber(pagePost.getEvaluatedPeopleNumber())
                .setEvaluationContentNumber(pagePost.getEvaluationContentNumber())
                .setQuestionnaireCode(pagePost.getQuestionnaireCode());
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            log.error("sleep error", e);
        }
        evaluationSpider.evaluate(post);
    }

    private Term getTerm() {
        return schoolTimeManager.getCurrentSchoolTime().getTerm();
    }

    @Lookup
    public UrpEvaluationSpider getUrpEvaluationSpider(){
        return null;
    }
}
