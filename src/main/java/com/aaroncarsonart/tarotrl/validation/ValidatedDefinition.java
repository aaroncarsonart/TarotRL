package com.aaroncarsonart.tarotrl.validation;

import java.util.List;

/**
 * A simple interface for objects that require validation.
 *
 * @see com.aaroncarsonart.tarotrl.validation.DefinitionValidator DefinitionValidator.
 * @see com.aaroncarsonart.tarotrl.exception.ValidatedDefinitionException ValidatedDefinitionException
 */
public abstract class ValidatedDefinition {

    /**
     * Define any custom validation logic for the given type.
     * @return A list of validation errors.
     *         If the list is empty or null, the object is valid.
     *         If a non-empty list is returned, the object is invalid.
     */
    public abstract List<String> validate();
}
