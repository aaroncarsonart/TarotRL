package com.aaroncarsonart.tarotrl.exception;

import com.aaroncarsonart.tarotrl.util.JsonUtils;

import java.util.List;

/**
 * The Exception type thrown by ValidatedDefinition when validated by the Definition Validator.
 *
 * @see com.aaroncarsonart.tarotrl.validation.DefinitionValidator
 * @see com.aaroncarsonart.tarotrl.validation.ValidatedDefinition
 */
public class ValidatedDefinitionException extends Exception {

    private String className;
    private String definitionFilePath;
    private List<String> validationErrors;

    public ValidatedDefinitionException(String className, List<String> validationErrors) {
        super();
        this.className = className;
        this.validationErrors = validationErrors;
    }

    /**
     * Provide more relevant debugging info if the path to the source file
     * for the given definition throwing this exception is readily available.
     * @param definitionFilePath The path to the original definition file.
     */
    public void setDefinitionFilePath(String definitionFilePath) {
        this.definitionFilePath = definitionFilePath;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    /**
     * An extra POJO class to conveniently print only this class's fields
     * as a simply formatted JSON string. (Exclude all the other Exception
     * related fields from printing.)
     */
    private class ValidatedDefinitionExceptionPOJO {
        private String className;
        private String definitionFilePath;
        private List<String> validationErrors;

        ValidatedDefinitionExceptionPOJO(ValidatedDefinitionException that) {
            this.className = that.className;
            this.definitionFilePath = that.definitionFilePath;
            this.validationErrors = that.validationErrors;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getDefinitionFilePath() {
            return definitionFilePath;
        }

        public void setDefinitionFilePath(String definitionFilePath) {
            this.definitionFilePath = definitionFilePath;
        }

        public List<String> getValidationErrors() {
            return validationErrors;
        }

        public void setValidationErrors(List<String> validationErrors) {
            this.validationErrors = validationErrors;
        }
    }

    @Override
    public String toString() {
        ValidatedDefinitionExceptionPOJO pojo = new ValidatedDefinitionExceptionPOJO(this);
        return super.toString() + " " + JsonUtils.writeValueAsString(pojo);
    }
}
