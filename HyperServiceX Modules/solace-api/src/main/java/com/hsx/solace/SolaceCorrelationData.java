package com.hsx.solace;

import com.hsx.solace.producer.SolaceProducerCallback;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SolaceCorrelationData {
    private int siteNo;
    private String topic;
    private String correlationData;
    private SolaceProducerCallback callback;
}
