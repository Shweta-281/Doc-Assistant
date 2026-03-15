package org.shweta.docassistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AiResponse {

    private String answer;
    private List<String> source;
}
