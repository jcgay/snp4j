package com.github.jcgay.snp4j.request;

import com.github.jcgay.snp4j.Application;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Request {

    @NonNull
    private final List<Action> actions;
    @NonNull
    private final Application application;

    public static Builder builder(@NonNull Application application) {
        return new Builder(application);
    }

    public static class Builder {

        private Application application;
        private List<Action> actions = new ArrayList<Action>();

        protected Builder(Application application) {
            this.application = application;
        }

        public Builder addAction(@NonNull Action action) {
            this.actions.add(action);
            return this;
        }

        public Request build() {
            return new Request(unmodifiableList(actions), application);
        }
    }
}
