package com.raizlabs.android.broker.compiler.definition;

import com.raizlabs.android.broker.compiler.RequestManager;

/**
 * Description: Validates RestMethod data to ensure proper usage
 */
public class RestMethodValidator implements Validator<RestMethodDefinition> {

    @Override
    public boolean validate(RequestManager requestManager, RestMethodDefinition restMethodDefinition) {
        boolean success = true;

        boolean isValid = restMethodDefinition.returnType != null
                && (restMethodDefinition.returnsRequest || restMethodDefinition.returnsRequestBuilder
                || restMethodDefinition.returnsVoid);
        if (!isValid) {
            requestManager.logError("RestMethod %1s must return either a void, Request, or Request.Builder",
                    restMethodDefinition.elementName);
            success = false;
        }

        if (restMethodDefinition.url == null || restMethodDefinition.url.trim().isEmpty()) {
            requestManager.logError("RestMethod %1s must have an end url defined",
                    restMethodDefinition.elementName);
            success = false;
        }

        if (restMethodDefinition.body != null && !restMethodDefinition.body.isEmpty()
                && !restMethodDefinition.partMap.isEmpty()) {
            requestManager.logError("A Multipart request for RestMethod %1s will override the body of request",
                    restMethodDefinition.elementName);
            success = false;
        }

        return success;
    }
}
