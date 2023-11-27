package fa.training.interviewmanagement.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = PhoneValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    String message() default "{Phone}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
