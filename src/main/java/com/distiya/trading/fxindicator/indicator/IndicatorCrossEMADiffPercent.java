package com.distiya.trading.fxindicator.indicator;

import com.distiya.trading.fxindicator.constant.IndicatorLogicApplier;
import com.distiya.trading.fxindicator.dto.CandleData;
import com.distiya.trading.fxindicator.dto.IndicatorValue;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class IndicatorCrossEMADiffPercent {

    private IndicatorCrossEMA crossEMA;
    private IndicatorDiffPercent slowEMADiffPercent;
    private IndicatorDiffPercent fastEMADiffPercent;
    @Getter
    private Queue<IndicatorValue> history;
    @Setter
    private Integer historyCount;

    public IndicatorCrossEMADiffPercent(Integer slowPeriod,Integer fastPeriod){
        this(slowPeriod,fastPeriod,2);
    }

    public IndicatorCrossEMADiffPercent(Integer slowPeriod,Integer fastPeriod,Integer historyCount){
        this.historyCount = historyCount;
        if(this.historyCount > 0)
            this.history = new PriorityQueue<>(historyCount, Comparator.comparing(IndicatorValue::getTime));
        this.crossEMA = new IndicatorCrossEMA(slowPeriod,fastPeriod,0);
        this.slowEMADiffPercent = new IndicatorDiffPercent(0);
        this.fastEMADiffPercent = new IndicatorDiffPercent(0);
    }

    public void update(CandleData candleData, IndicatorLogicApplier indicatorLogicApplier){
        this.crossEMA.update(candleData,indicatorLogicApplier);
        if(this.crossEMA.getSlowEMA() != -300.0d){
            double currentValue;
            switch (indicatorLogicApplier){
                case FOR_LOW : currentValue = candleData.getLow();break;
                case FOR_HIGH : currentValue = candleData.getHigh();break;
                case FOR_OPEN : currentValue = candleData.getOpen();break;
                default : currentValue = candleData.getClose();
            }
            this.fastEMADiffPercent.update(candleData.getTime(),this.crossEMA.getFastEMA(),currentValue);
            this.slowEMADiffPercent.update(candleData.getTime(),this.crossEMA.getSlowEMA(),currentValue);
            updateHistory(candleData);
        }
    }

    public void update(CandleData candleData){
        update(candleData,IndicatorLogicApplier.FOR_CLOSE);
    }

    public Double getFastEMAPriceDiffPercent(){
        if(this.crossEMA.getSlowEMA() == -300.0d)
            return -300.0d;
        else
            return this.fastEMADiffPercent.getValue();
    }

    public Double getSlowEMAPriceDiffPercent(){
        if(this.crossEMA.getSlowEMA() == -300.0d)
            return -300.0d;
        else
            return this.slowEMADiffPercent.getValue();
    }

    private void updateHistory(CandleData candleData){
        if(this.history != null){
            if(this.history.size() > 0 && this.history.size() % this.historyCount == 0)
                this.history.poll();
            this.history.add(new IndicatorValue(candleData.getTime(),new double[]{this.slowEMADiffPercent.getValue(),this.fastEMADiffPercent.getValue()}));
        }
    }

}
