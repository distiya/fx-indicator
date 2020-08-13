package com.distiya.trading.fxindicator.indicator;

import com.distiya.trading.fxindicator.constant.IndicatorLogicApplier;
import com.distiya.trading.fxindicator.dto.CandleData;
import com.distiya.trading.fxindicator.dto.IndicatorValue;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class IndicatorRSI {

    public IndicatorRSI(Integer period,IndicatorLogicApplier indicatorLogicApplier){
        this(period,2,indicatorLogicApplier);
    }

    public IndicatorRSI(Integer period,Integer historyCount,IndicatorLogicApplier indicatorLogicApplier){
        this.period = period;
        this.indicatorLogicApplier = indicatorLogicApplier;
        this.historyCount = historyCount;
        if(this.historyCount > 0)
            this.history = new PriorityQueue<>(historyCount, Comparator.comparing(IndicatorValue::getTime));
        this.upwardGain = new IndicatorSMA(period,0);
        this.downwardGain = new IndicatorSMA(period,historyCount);
    }

    @Setter
    private Integer period;
    @Setter
    private Integer historyCount;
    @Getter
    private Double value = -300.0d;
    @Getter
    private Queue<IndicatorValue> history;
    private IndicatorSMA upwardGain;
    private IndicatorSMA downwardGain;
    private Double previousValue;
    private IndicatorLogicApplier indicatorLogicApplier;

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
        update(candleData,indicatorLogicApplier);
    }

    public void update(Double currentValue, LocalDateTime candleTime){
        if(previousValue != null && currentValue != null){
            double upGain;
            double downGain;
            upGain = (currentValue > previousValue) ? currentValue - previousValue : 0.0;
            downGain = (previousValue > currentValue) ? previousValue - currentValue : 0.0;
            upwardGain.update(upGain,candleTime);
            downwardGain.update(downGain,candleTime);
            if(upwardGain.getSmaPeriodProgressCount() >= period){
                this.value = 100.0 - (100.0/(1+(upwardGain.getValue()/downwardGain.getValue())));
                updateHistory(candleTime);
            }
        }
        previousValue = currentValue;
    }

    private void updateHistory(LocalDateTime lastCandleTime){
        if(this.history != null){
            if(this.history.size() > 0 && this.history.size() % this.historyCount == 0)
                this.history.poll();
            this.history.add(new IndicatorValue(lastCandleTime,new double[]{this.value}));
        }
    }
}
