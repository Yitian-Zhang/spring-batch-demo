package com.yitian.examples.springbatchdemo.demo1;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by yitian.zyt on 2021/7/11
 */
public class BlogInfoValidator<T> implements Validator<T>, InitializingBean {
    private javax.validation.Validator validator;

    /**
     * 使用JSR-303的Validator来校验我们的数据，在此进行JSR-303的Validator的初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    /**
     * 使用Validator的validate方法校验数据
     * @param t
     * @throws ValidationException
     */
    @Override
    public void validate(T t) throws ValidationException {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);

        if (constraintViolations.size() > 0) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<T> violation : constraintViolations) {
                msg.append(violation.getMessage() + "\n");
            }
            throw new ValidationException(msg.toString());
        }
    }
}
