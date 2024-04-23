package com.datastat.aop.moderation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.datastat.dao.RedisDao;
import com.datastat.util.ModerationUtil;
import java.lang.annotation.RetentionPolicy;


import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;


@Target({ElementType.FIELD, ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ModerationValid.List.class)
@Constraint(validatedBy = ModerationValid.ModerationValidator.class)
public @interface ModerationValid {
    String message() default "Parameter contains sensitive words";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD, ElementType.PARAMETER})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		ModerationValid[] value();
	}

	class ModerationValidator implements ConstraintValidator<ModerationValid, String> {
        @Autowired
        RedisDao redisDao;
    
        @Autowired
        Environment env;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            String token = (String) redisDao.get("nps_moderation_token");
            if (token == null) {
                token = ModerationUtil.getHuaweiCloudToken(env.getProperty("moderation.body.format"),
                        env.getProperty("moderation.user.name"), env.getProperty("moderation.user.password"),
                        env.getProperty("moderation.domain.name"), env.getProperty("moderation.token.endpoint"));
                redisDao.set("nps_moderation_token", token, 36000l);
            }
            if (ModerationUtil.moderation(env.getProperty("moderation.url"), value, token))
                return true;
            return false;
        }

    }
}