package dk.nykredit.infra.rest;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
@InterceptorBinding
public @interface RestSecured {
}
