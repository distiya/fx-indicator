package com.distiya.trading.fxindicator.indicator;

import com.distiya.trading.fxindicator.dto.IndicatorValue;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class IndicatorDiffPercent {

    public IndicatorDiffPercent(){
        this(2);
    }

    public IndicatorDiffPercent(Integer historyCount){
        this.historyCount = historyCount;
        if(this.historyCount > 0)
            this.history = new PriorityQueue<>(historyCount, Comparator.comparing(IndicatorValue::getTime));
    }

    @Setter
    private Integer historyCount;
    @Getter
    private Queue<IndicatorValue> history;
    @Getter
    private Double value = 0.0;

    public void update(LocalDateTime time,Double baseValue,Double comparedValue){
        this.value = ((comparedValue - baseValue)/baseValue) * 100.0;
        updateHistory(time);
    }

    private void updateHistory(LocalDateTime time){
        if(this.history != null){
            if(this.history.size() > 0 && this.history.size() % this.historyCount == 0)
                this.history.poll();
            this.history.add(new IndicatorValue(time,new double[]{this.value}));
        }
    }
}
