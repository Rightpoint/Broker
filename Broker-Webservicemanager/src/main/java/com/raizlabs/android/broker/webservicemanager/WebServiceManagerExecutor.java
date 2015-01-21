package com.raizlabs.android.broker.webservicemanager;

import com.raizlabs.android.broker.core.Priority;
import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestExecutor;
import com.raizlabs.concurrent.Prioritized;
import com.raizlabs.net.webservicemanager.WebServiceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Executes the requests using our {@link com.raizlabs.net.webservicemanager.WebServiceManager}.
 */
public class WebServiceManagerExecutor implements RequestExecutor<Void> {

    private WebServiceManager mManager = new WebServiceManager();

    private final List<BrokerWebServiceRequest> mRequests = new ArrayList<>();

    public WebServiceManager getWebServiceManager() {
        return mManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(final Request request) {
        BrokerWebServiceRequest brokerWebServiceRequest = new BrokerWebServiceRequest(request);
        brokerWebServiceRequest.execute(this);
    }

    void addRequest(BrokerWebServiceRequest brokerWebServiceRequest) {
        synchronized (mRequests) {
            mRequests.add(brokerWebServiceRequest);
        }
    }

    void removeRequest(BrokerWebServiceRequest brokerWebServiceRequest) {
        synchronized (mRequests) {
            mRequests.remove(brokerWebServiceRequest);
        }
    }

    public int getActiveRequestSize() {
        synchronized (mRequests) {
            return mRequests.size();
        }
    }

    @Override
    public void cancelRequest(Void aVoid, Request request) {
        synchronized (mRequests) {
            int removePosition = -1;
            for (int i = 0; i < mRequests.size(); i++) {
                BrokerWebServiceRequest brokerWebServiceRequest = mRequests.get(i);
                if (brokerWebServiceRequest.getRequest().equals(request)) {
                    removePosition = i;
                    break;
                }
            }
            if (removePosition != -1) {
                BrokerWebServiceRequest webServiceRequest = mRequests.remove(removePosition);
                webServiceRequest.cancel();
            }
        }
    }

    @Override
    public void cancelAllRequests() {
        synchronized (mRequests) {
            for (BrokerWebServiceRequest brokerWebServiceRequest : mRequests) {
                brokerWebServiceRequest.cancel();
            }
            mRequests.clear();
        }
    }

    @Override
    public Integer convertPriority(Priority priority) {
        switch (priority) {
            case IMMEDIATE:
                return Prioritized.Priority.IMMEDIATE;
            case HIGH:
                return Prioritized.Priority.FOREGROUND;
            case NORMAL:
                return Prioritized.Priority.NORMAL;
            case LOW:
                return Prioritized.Priority.BACKGROUND;
        }
        return null;
    }
}
