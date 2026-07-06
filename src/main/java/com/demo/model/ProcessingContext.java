package com.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter@Getter
public class ProcessingContext {

    private String loggingId;

    private Long originalId;

    private String stage;

    private Message payload;

}