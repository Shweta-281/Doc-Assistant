package org.shweta.docassistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SourceChunk {

    private String content;
    private String fileName;
}
