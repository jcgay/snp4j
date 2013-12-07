package com.github.jcgay.snp4j.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Action {

    @NonNull
    private final String name;
    @NonNull
    private final List<Parameter> parameters;

    public static Builder name(@NonNull String name) {
        return new Builder(name);
    }

    public static class Builder {
        private String name;
        private List<Parameter> parameters = new ArrayList<Parameter>();

        protected Builder(String name) {
            this.name = name;
        }

        public Builder withParameter(@NonNull Parameter parameter) {
            if (!parameter.equals(EmptyParameter.empty())) {
                this.parameters.add(parameter);
            }
            return this;
        }

        public Builder withParameter(@NonNull String key, Object value) {
            if (value != null) {
                this.parameters.add(Parameter.of(key, value));
            }
            return this;
        }

        public Action build() {
            return new Action(name, unmodifiableList(parameters));
        }
    }
}
