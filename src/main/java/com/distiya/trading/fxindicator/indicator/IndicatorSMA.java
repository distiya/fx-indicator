package com.distiya.trading.fxindicator.indicator;

import com.distiya.trading.fxindicator.constant.IndicatorLogicApplier;
import com.distiya.trading.fxindicator.dto.CandleData;
import com.distiya.trading.fxindicator.dto.IndicatorValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

@NoArgsConstructor
public class IndicatorSMA {

    public IndicatorSMA(Integer period){
        this(period,2);
    }

    public IndicatorSMA(Integer period,Integer historyCount){
        this.period = period;
        this.historyCount = historyCount;
        if(this.historyCount > 0)
            this.history = new PriorityQueue<>(historyCount, Comparator.comparing(IndicatorValue::getTime));
    }

    @Setter
    private Integer period;
    @Setter
    private Integer historyCount;
    private Double value = 0.0;
    @Getter
    private Queue<IndicatorValue> history;
    @Getter
    private Integer smaPeriodProgressCount = 0;
    private LocalDateTime lastCandleTime;

    public void update(CandleData candleData, IndicatorLogicApplier indicatorLogicApplier){
        double currentValue;
        switch (indicatorLogicApplier){
            case FOR_LOW : currentValue = candleData.getLow();break;
            case FOR_HIGH : currentValue = candleData.getHigh();break;
            case FOR_OPEN : currentValue = candleData.getOpen();break;
            default : currentValue = candleData.getClose();
        }
        update(currentValue,candleData.getTime());
    }

    public void update(CandleData candleData){
        update(candleData,IndicatorLogicApplier.FOR_CLOSE);
    }

    public void update(Double currentValue, LocalDateTime candleTime){
        if(this.smaPeriodProgressCount<this.period){
            this.value += currentValue/this.period;
            this.lastCandleTime = candleTime;
            this.smaPeriodProgressCount++;
        }
        else{
            updateHistory();
            this.value = this.value + (currentValue - this.value)/this.period;
            this.lastCandleTime = candleTime;
        }
    }

    public Double getValue(){
        if(this.smaPeriodProgressCount < this.period)
            return -300.0d;
        else
            return this.value;
    }

    private void updateHistory(){
        if(this.history != null){
            if(this.history.size() > 0 && this.history.size() % this.historyCount == 0)
                this.history.poll();
            this.history.add(new IndicatorValue(this.lastCandleTime,new double[]{this.value}));
        }
    }

}
