package com.raizlabs.android.broker.compiler.definition;

import com.raizlabs.android.broker.compiler.RequestManager;

/**
 * Description: Validates annotations.
 */
public interface Validator<ValidatorDefinition> {

    /**
     * @param requestManager The manager that manages this compiler
     * @param validatorDefinition The validator to use
     *
     * @return true
     */
    public boolean validate(RequestManager requestManager, ValidatorDefinition validatorDefinition);
}
