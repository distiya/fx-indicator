package com.distiya.trading.fxindicator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandleData {
    private LocalDateTime time;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Double volume;
}
