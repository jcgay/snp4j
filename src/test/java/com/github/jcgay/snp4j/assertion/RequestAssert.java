package com.github.jcgay.snp4j.assertion;

import com.github.jcgay.snp4j.Application;
import com.github.jcgay.snp4j.impl.request.Action;
import com.github.jcgay.snp4j.impl.request.Parameter;
import com.github.jcgay.snp4j.impl.request.Request;
import org.assertj.core.api.AbstractAssert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestAssert extends AbstractAssert<RequestAssert, Request> {

    private Map<String, List<Parameter>> parametersByAction = new HashMap<String, List<Parameter>>();

    public RequestAssert(Request actual) {
        super(actual, RequestAssert.class);
        for (Action action : actual.getActions()) {
            parametersByAction.put(action.getName(), action.getParameters());
        }
    }

    public static RequestAssert assertThat(Request actual) {
        return new RequestAssert(actual);
    }

    public RequestAssert hasApplication(Application application) {
        isNotNull();
        checkNotNull(application);
        if (!application.equals(actual.getApplication())) {
            failWithMessage("Expected application to equals <%s> but was <%s>.", application, actual.getApplication());
        }
        return this;
    }

    public RequestAssert containsEntry(String actionName, Parameter parameter) {
        isNotNull();
        checkNotNull(actionName);
        checkNotNull(parameter);
        if (!parametersByAction.containsKey(actionName)) {
            failWithMessage("Expected request with action <%s> but action was not present.", actionName);
        }
        if (!parametersByAction.get(actionName).contains(parameter)) {
            failWithMessage("Expected parameter <%s> to be defined for action <%s> but was not.", parameter, actionName);
        }
        return this;
    }

    private void checkNotNull(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Expected value cannot be null.");
        }
    }
}
