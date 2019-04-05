package com.aaroncarsonart.tarotrl.validation;

import com.aaroncarsonart.tarotrl.exception.ValidatedDefinitionException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple validator that validates a given ValidatedDefinition instance.
 *
 * @see com.aaroncarsonart.tarotrl.validation.ValidatedDefinition
 * @see com.aaroncarsonart.tarotrl.exception.ValidatedDefinitionException
 */
public class DefinitionValidator {

    private final Validator validator;

    public DefinitionValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Validate the given object. (Throws an exception if an error is found.)
     */
    public final void doValidate(Object needToValidate) throws ValidatedDefinitionException {
        List<String> validationErrors = new ArrayList<>();

        // first, collect all default constraint violations.
        List<String> constraintViolationErrors = getConstraintViolations(needToValidate);
        validationErrors.addAll(constraintViolationErrors);

        // if there were none, then collect the custom validation errors.
        if (validationErrors.isEmpty() && needToValidate instanceof ValidatedDefinition) {
            ValidatedDefinition needToCustomValidate = (ValidatedDefinition) needToValidate;
            List<String> customValidationErrors = needToCustomValidate.validate();
            if (customValidationErrors != null) {
                validationErrors.addAll(customValidationErrors);
            }
        }

        // report if any validation errors were found.
        if (!validationErrors.isEmpty()) {
            String className = needToValidate.getClass().getSimpleName();
            throw new ValidatedDefinitionException(className, validationErrors);
        }
    }

    /**
     * Get the basic constaint violation errors, add them to the list of error messages.
     */
    public List<String> getConstraintViolations(Object needToValidate) {
        Set<ConstraintViolation<Object>> errors = validator.validate(needToValidate);
        List<String> constraintViolationErrors = new ArrayList<>();
        for (ConstraintViolation<Object> error : errors) {
            String errorMessage = error.getPropertyPath() + " " + error.getMessage();
            constraintViolationErrors.add(errorMessage);
        }
        return constraintViolationErrors;
    }
}
