package com.kenfogel.luhncheck;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Custom annotation for bean validation
 *
 * @author Ken Fogel
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = LuhnCheckValidator.class)
public @interface LuhnCheck {

    String message() default "{com.trouble.bundles.messages.failLuhn}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
