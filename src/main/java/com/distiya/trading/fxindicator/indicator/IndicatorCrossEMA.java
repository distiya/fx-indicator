package com.distiya.trading.fxindicator.indicator;

import com.distiya.trading.fxindicator.constant.IndicatorLogicApplier;
import com.distiya.trading.fxindicator.dto.CandleData;
import com.distiya.trading.fxindicator.dto.IndicatorValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

@NoArgsConstructor
public class IndicatorCrossEMA {

    private IndicatorEMA slowEMA;
    private IndicatorEMA fastEMA;
    @Getter
    private Queue<IndicatorValue> history;
    @Setter
    private Integer historyCount;
    @Setter
    private Integer slowPeriod;
    @Setter
    private Integer fastPeriod;

    public IndicatorCrossEMA(Integer slowPeriod,Integer fastPeriod){
        this(slowPeriod,fastPeriod,2);
    }

    public IndicatorCrossEMA(Integer slowPeriod,Integer fastPeriod,Integer historyCount){
        this.historyCount = historyCount;
        if(this.historyCount > 0)
            this.history = new PriorityQueue<>(historyCount, Comparator.comparing(IndicatorValue::getTime));
        this.slowEMA = new IndicatorEMA(slowPeriod,0);
        this.fastEMA = new IndicatorEMA(fastPeriod,0);
        this.slowPeriod = slowPeriod;
        this.fastPeriod = fastPeriod;
    }

    public void update(CandleData candleData, IndicatorLogicApplier indicatorLogicApplier){
        this.slowEMA.update(candleData,indicatorLogicApplier);
        this.fastEMA.update(candleData,indicatorLogicApplier);
        if(this.slowEMA.getEmaPeriodProgressCount() >= this.slowPeriod)
            updateHistory(candleData);

    }

    public void update(CandleData candleData){
        update(candleData,IndicatorLogicApplier.FOR_CLOSE);
    }

    public Double getSlowEMA(){
        if(this.slowEMA.getEmaPeriodProgressCount() < this.slowPeriod)
            return -300.0d;
        else
            return this.slowEMA.getValue();
    }

    public Double getFastEMA(){
        if(this.slowEMA.getEmaPeriodProgressCount() < this.slowPeriod)
            return -300.0d;
        else
            return this.fastEMA.getValue();
    }

    private void updateHistory(CandleData candleData){
        if(this.history != null){
            if(this.history.size() > 0 && this.history.size() % this.historyCount == 0)
                this.history.poll();
            this.history.add(new IndicatorValue(candleData.getTime(),new double[]{this.slowEMA.getValue(),this.fastEMA.getValue()}));
        }
    }
}
