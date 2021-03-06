package com.hackerda.platform.infrastructure.database.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppreciateExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AppreciateExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andLikeTypeIsNull() {
            addCriterion("like_type is null");
            return (Criteria) this;
        }

        public Criteria andLikeTypeIsNotNull() {
            addCriterion("like_type is not null");
            return (Criteria) this;
        }

        public Criteria andLikeTypeEqualTo(Integer value) {
            addCriterion("like_type =", value, "likeType");
            return (Criteria) this;
        }

        public Criteria andLikeTypeNotEqualTo(Integer value) {
            addCriterion("like_type <>", value, "likeType");
            return (Criteria) this;
        }

        public Criteria andLikeTypeGreaterThan(Integer value) {
            addCriterion("like_type >", value, "likeType");
            return (Criteria) this;
        }

        public Criteria andLikeTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("like_type >=", value, "likeType");
            return (Criteria) this;
        }

        public Criteria andLikeTypeLessThan(Integer value) {
            addCriterion("like_type <", value, "likeType");
            return (Criteria) this;
        }

        public Criteria andLikeTypeLessThanOrEqualTo(Integer value) {
            addCriterion("like_type <=", value, "likeType");
            return (Criteria) this;
        }

        public Criteria andLikeTypeIn(List<Integer> values) {
            addCriterion("like_type in", values, "likeType");
            return (Criteria) this;
        }

        public Criteria andLikeTypeNotIn(List<Integer> values) {
            addCriterion("like_type not in", values, "likeType");
            return (Criteria) this;
        }

        public Criteria andLikeTypeBetween(Integer value1, Integer value2) {
            addCriterion("like_type between", value1, value2, "likeType");
            return (Criteria) this;
        }

        public Criteria andLikeTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("like_type not between", value1, value2, "likeType");
            return (Criteria) this;
        }

        public Criteria andTypeContentIdIsNull() {
            addCriterion("type_content_id is null");
            return (Criteria) this;
        }

        public Criteria andTypeContentIdIsNotNull() {
            addCriterion("type_content_id is not null");
            return (Criteria) this;
        }

        public Criteria andTypeContentIdEqualTo(Long value) {
            addCriterion("type_content_id =", value, "typeContentId");
            return (Criteria) this;
        }

        public Criteria andTypeContentIdNotEqualTo(Long value) {
            addCriterion("type_content_id <>", value, "typeContentId");
            return (Criteria) this;
        }

        public Criteria andTypeContentIdGreaterThan(Long value) {
            addCriterion("type_content_id >", value, "typeContentId");
            return (Criteria) this;
        }

        public Criteria andTypeContentIdGreaterThanOrEqualTo(Long value) {
            addCriterion("type_content_id >=", value, "typeContentId");
            return (Criteria) this;
        }

        public Criteria andTypeContentIdLessThan(Long value) {
            addCriterion("type_content_id <", value, "typeContentId");
            return (Criteria) this;
        }

        public Criteria andTypeContentIdLessThanOrEqualTo(Long value) {
            addCriterion("type_content_id <=", value, "typeContentId");
            return (Criteria) this;
        }

        public Criteria andTypeContentIdIn(List<Long> values) {
            addCriterion("type_content_id in", values, "typeContentId");
            return (Criteria) this;
        }

        public Criteria andTypeContentIdNotIn(List<Long> values) {
            addCriterion("type_content_id not in", values, "typeContentId");
            return (Criteria) this;
        }

        public Criteria andTypeContentIdBetween(Long value1, Long value2) {
            addCriterion("type_content_id between", value1, value2, "typeContentId");
            return (Criteria) this;
        }

        public Criteria andTypeContentIdNotBetween(Long value1, Long value2) {
            addCriterion("type_content_id not between", value1, value2, "typeContentId");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andIdentityCodeIsNull() {
            addCriterion("identity_code is null");
            return (Criteria) this;
        }

        public Criteria andIdentityCodeIsNotNull() {
            addCriterion("identity_code is not null");
            return (Criteria) this;
        }

        public Criteria andIdentityCodeEqualTo(Integer value) {
            addCriterion("identity_code =", value, "identityCode");
            return (Criteria) this;
        }

        public Criteria andIdentityCodeNotEqualTo(Integer value) {
            addCriterion("identity_code <>", value, "identityCode");
            return (Criteria) this;
        }

        public Criteria andIdentityCodeGreaterThan(Integer value) {
            addCriterion("identity_code >", value, "identityCode");
            return (Criteria) this;
        }

        public Criteria andIdentityCodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("identity_code >=", value, "identityCode");
            return (Criteria) this;
        }

        public Criteria andIdentityCodeLessThan(Integer value) {
            addCriterion("identity_code <", value, "identityCode");
            return (Criteria) this;
        }

        public Criteria andIdentityCodeLessThanOrEqualTo(Integer value) {
            addCriterion("identity_code <=", value, "identityCode");
            return (Criteria) this;
        }

        public Criteria andIdentityCodeIn(List<Integer> values) {
            addCriterion("identity_code in", values, "identityCode");
            return (Criteria) this;
        }

        public Criteria andIdentityCodeNotIn(List<Integer> values) {
            addCriterion("identity_code not in", values, "identityCode");
            return (Criteria) this;
        }

        public Criteria andIdentityCodeBetween(Integer value1, Integer value2) {
            addCriterion("identity_code between", value1, value2, "identityCode");
            return (Criteria) this;
        }

        public Criteria andIdentityCodeNotBetween(Integer value1, Integer value2) {
            addCriterion("identity_code not between", value1, value2, "identityCode");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameIsNull() {
            addCriterion("reply_user_name is null");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameIsNotNull() {
            addCriterion("reply_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameEqualTo(String value) {
            addCriterion("reply_user_name =", value, "replyUserName");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameNotEqualTo(String value) {
            addCriterion("reply_user_name <>", value, "replyUserName");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameGreaterThan(String value) {
            addCriterion("reply_user_name >", value, "replyUserName");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("reply_user_name >=", value, "replyUserName");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameLessThan(String value) {
            addCriterion("reply_user_name <", value, "replyUserName");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameLessThanOrEqualTo(String value) {
            addCriterion("reply_user_name <=", value, "replyUserName");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameLike(String value) {
            addCriterion("reply_user_name like", value, "replyUserName");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameNotLike(String value) {
            addCriterion("reply_user_name not like", value, "replyUserName");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameIn(List<String> values) {
            addCriterion("reply_user_name in", values, "replyUserName");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameNotIn(List<String> values) {
            addCriterion("reply_user_name not in", values, "replyUserName");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameBetween(String value1, String value2) {
            addCriterion("reply_user_name between", value1, value2, "replyUserName");
            return (Criteria) this;
        }

        public Criteria andReplyUserNameNotBetween(String value1, String value2) {
            addCriterion("reply_user_name not between", value1, value2, "replyUserName");
            return (Criteria) this;
        }

        public Criteria andLikeTimeIsNull() {
            addCriterion("like_time is null");
            return (Criteria) this;
        }

        public Criteria andLikeTimeIsNotNull() {
            addCriterion("like_time is not null");
            return (Criteria) this;
        }

        public Criteria andLikeTimeEqualTo(Date value) {
            addCriterion("like_time =", value, "likeTime");
            return (Criteria) this;
        }

        public Criteria andLikeTimeNotEqualTo(Date value) {
            addCriterion("like_time <>", value, "likeTime");
            return (Criteria) this;
        }

        public Criteria andLikeTimeGreaterThan(Date value) {
            addCriterion("like_time >", value, "likeTime");
            return (Criteria) this;
        }

        public Criteria andLikeTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("like_time >=", value, "likeTime");
            return (Criteria) this;
        }

        public Criteria andLikeTimeLessThan(Date value) {
            addCriterion("like_time <", value, "likeTime");
            return (Criteria) this;
        }

        public Criteria andLikeTimeLessThanOrEqualTo(Date value) {
            addCriterion("like_time <=", value, "likeTime");
            return (Criteria) this;
        }

        public Criteria andLikeTimeIn(List<Date> values) {
            addCriterion("like_time in", values, "likeTime");
            return (Criteria) this;
        }

        public Criteria andLikeTimeNotIn(List<Date> values) {
            addCriterion("like_time not in", values, "likeTime");
            return (Criteria) this;
        }

        public Criteria andLikeTimeBetween(Date value1, Date value2) {
            addCriterion("like_time between", value1, value2, "likeTime");
            return (Criteria) this;
        }

        public Criteria andLikeTimeNotBetween(Date value1, Date value2) {
            addCriterion("like_time not between", value1, value2, "likeTime");
            return (Criteria) this;
        }

        public Criteria andShowIsNull() {
            addCriterion("`show` is null");
            return (Criteria) this;
        }

        public Criteria andShowIsNotNull() {
            addCriterion("`show` is not null");
            return (Criteria) this;
        }

        public Criteria andShowEqualTo(Byte value) {
            addCriterion("`show` =", value, "show");
            return (Criteria) this;
        }

        public Criteria andShowNotEqualTo(Byte value) {
            addCriterion("`show` <>", value, "show");
            return (Criteria) this;
        }

        public Criteria andShowGreaterThan(Byte value) {
            addCriterion("`show` >", value, "show");
            return (Criteria) this;
        }

        public Criteria andShowGreaterThanOrEqualTo(Byte value) {
            addCriterion("`show` >=", value, "show");
            return (Criteria) this;
        }

        public Criteria andShowLessThan(Byte value) {
            addCriterion("`show` <", value, "show");
            return (Criteria) this;
        }

        public Criteria andShowLessThanOrEqualTo(Byte value) {
            addCriterion("`show` <=", value, "show");
            return (Criteria) this;
        }

        public Criteria andShowIn(List<Byte> values) {
            addCriterion("`show` in", values, "show");
            return (Criteria) this;
        }

        public Criteria andShowNotIn(List<Byte> values) {
            addCriterion("`show` not in", values, "show");
            return (Criteria) this;
        }

        public Criteria andShowBetween(Byte value1, Byte value2) {
            addCriterion("`show` between", value1, value2, "show");
            return (Criteria) this;
        }

        public Criteria andShowNotBetween(Byte value1, Byte value2) {
            addCriterion("`show` not between", value1, value2, "show");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNull() {
            addCriterion("gmt_create is null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNotNull() {
            addCriterion("gmt_create is not null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateEqualTo(Date value) {
            addCriterion("gmt_create =", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotEqualTo(Date value) {
            addCriterion("gmt_create <>", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThan(Date value) {
            addCriterion("gmt_create >", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_create >=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThan(Date value) {
            addCriterion("gmt_create <", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThanOrEqualTo(Date value) {
            addCriterion("gmt_create <=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIn(List<Date> values) {
            addCriterion("gmt_create in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotIn(List<Date> values) {
            addCriterion("gmt_create not in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateBetween(Date value1, Date value2) {
            addCriterion("gmt_create between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotBetween(Date value1, Date value2) {
            addCriterion("gmt_create not between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtModifyIsNull() {
            addCriterion("gmt_modify is null");
            return (Criteria) this;
        }

        public Criteria andGmtModifyIsNotNull() {
            addCriterion("gmt_modify is not null");
            return (Criteria) this;
        }

        public Criteria andGmtModifyEqualTo(Date value) {
            addCriterion("gmt_modify =", value, "gmtModify");
            return (Criteria) this;
        }

        public Criteria andGmtModifyNotEqualTo(Date value) {
            addCriterion("gmt_modify <>", value, "gmtModify");
            return (Criteria) this;
        }

        public Criteria andGmtModifyGreaterThan(Date value) {
            addCriterion("gmt_modify >", value, "gmtModify");
            return (Criteria) this;
        }

        public Criteria andGmtModifyGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_modify >=", value, "gmtModify");
            return (Criteria) this;
        }

        public Criteria andGmtModifyLessThan(Date value) {
            addCriterion("gmt_modify <", value, "gmtModify");
            return (Criteria) this;
        }

        public Criteria andGmtModifyLessThanOrEqualTo(Date value) {
            addCriterion("gmt_modify <=", value, "gmtModify");
            return (Criteria) this;
        }

        public Criteria andGmtModifyIn(List<Date> values) {
            addCriterion("gmt_modify in", values, "gmtModify");
            return (Criteria) this;
        }

        public Criteria andGmtModifyNotIn(List<Date> values) {
            addCriterion("gmt_modify not in", values, "gmtModify");
            return (Criteria) this;
        }

        public Criteria andGmtModifyBetween(Date value1, Date value2) {
            addCriterion("gmt_modify between", value1, value2, "gmtModify");
            return (Criteria) this;
        }

        public Criteria andGmtModifyNotBetween(Date value1, Date value2) {
            addCriterion("gmt_modify not between", value1, value2, "gmtModify");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}