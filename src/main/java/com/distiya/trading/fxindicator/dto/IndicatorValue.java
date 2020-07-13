package com.distiya.trading.fxindicator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IndicatorValue {

    private LocalDateTime time;
    private double[] values;

}
