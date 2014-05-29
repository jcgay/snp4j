package fr.jcgay.snp4j.request;

import lombok.Getter;

public enum Priority {
    HIGH(1),
    LOW(-1),
    NORMAL(0);

    @Getter
    private int value;

    Priority(int value) {
        this.value = value;
    }
}
