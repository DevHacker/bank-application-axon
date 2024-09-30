package com.gtechsolutions.bankapplication.commons.events;

import lombok.Getter;

public class BaseEvent <T>{
    @Getter
    private String id;

    public BaseEvent(T id) {
        this.id = id.toString();
    }
}
