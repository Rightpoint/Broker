package com.raizlabs.android.broker.core;

/**
 * Description: The priority that RequestExecutor converts into its own
 * priority system.
 */
public enum Priority {
    /**
     * A low priority request, we don't need it to happen immediately.
     */
    LOW,

    /**
     * A normal request.
     */
    NORMAL,

    /**
     * High valued request, we want to happen soon, but it's not related to UI directly.
     */
    HIGH,

    /**
     * Highest value, we want this to return immediately. It should be used for UI-related requests.
     */
    IMMEDIATE
}
