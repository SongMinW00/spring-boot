package com.example.demo.global.security.validate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Slf4j
/* Validator 를 구현한 AbstractValidator 생성 */
public abstract class AbstractValidator<T> implements Validator {
    @Override
    public boolean supports(Class<?> clazz){
        return true;
    }
    /* 컴파일러에서 경고하지 않도록 하기위해 사용 */
    @SuppressWarnings("unchecked")
    @Override
    /* validate 를 구현하고 검증로직이 들어가는 부분을 doValidate 로 따로 빼줌 */
    public void validate(Object target, Errors errors){
        try{
            doValidate((T) target, errors);
        } catch (RuntimeException e){
            log.error("중복 검증 에러", e);
            throw e;
        }
    }
    protected abstract void doValidate(final T dto, final  Errors errors);
}
