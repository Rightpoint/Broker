package com.raizlabs.android.broker.compiler.definition;

import com.raizlabs.android.broker.compiler.RequestManager;

/**
 * Description: Will check for valid base url.
 */
public class RestServiceValidator implements Validator<RestServiceDefinition> {

    @Override
    public boolean validate(RequestManager requestManager, RestServiceDefinition restServiceDefinition) {
        boolean success = true;

        boolean hasBaseUrl = !restServiceDefinition.baseUrlRes.isEmpty() || restServiceDefinition.baseUrlResId != 0;
        if (!hasBaseUrl) {
            requestManager.logError("RestService %1s must contain a base URL", restServiceDefinition.getFQCN());
            success = false;
        }

        return success;
    }
}
