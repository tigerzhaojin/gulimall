package com.tz.mall.product.exception;
/*
* 集中处理所有的controller异常*/


import com.tz.common.exception.BizCodeEnume;
import com.tz.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ResponseBody
//@ControllerAdvice(basePackages = "com.tz.mall.product.controller")
public class ExceptionController {
//    注解开启处理异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){
        log.info("数据校验问题：{},异常类型：{}",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String,String> errMap=new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(item->{
            errMap.put(item.getField(),item.getDefaultMessage());
        });
//       使用定在common里的统一错误码
        return R.error(BizCodeEnume.VALID_EXCEPTION.getCode(),BizCodeEnume.VALID_EXCEPTION.getMsg()).put("data",errMap);
    }

    @ExceptionHandler(value = Throwable.class)
    private R handleException(Throwable t){
        return R.error(BizCodeEnume.UNKNOWN_EXCEPTION.getCode(),
                BizCodeEnume.UNKNOWN_EXCEPTION.getMsg());
    }
}
