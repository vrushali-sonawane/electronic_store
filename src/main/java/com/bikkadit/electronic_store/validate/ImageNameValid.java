package com.bikkadit.electronic_store.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {

    //error message
    String message() default "Invalid Image name !!";

    // represent group of constraints
    Class<?>[] groups() default { };

    //additional information about constraint
    Class<? extends Payload>[] payload() default { };
}
