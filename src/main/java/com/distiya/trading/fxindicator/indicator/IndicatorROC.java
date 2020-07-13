package com.distiya.trading.fxindicator.indicator;

import com.distiya.trading.fxindicator.constant.IndicatorLogicApplier;
import com.distiya.trading.fxindicator.dto.CandleData;
import com.distiya.trading.fxindicator.dto.IndicatorValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

@NoArgsConstructor
public class IndicatorROC {

    public IndicatorROC(Integer period){
        this(period,2);
    }

    public IndicatorROC(Integer period,Integer historyCount){
        this.period = period;
        this.historyCount = historyCount;
        if(this.historyCount > 0)
            this.history = new PriorityQueue<>(historyCount, Comparator.comparing(IndicatorValue::getTime));
    }

    @Setter
    private Integer period;
    @Setter
    private Integer historyCount;
    @Getter
    private Double value = -300.0d;
    private Queue<Double> pastValues = new LinkedList<>();
    @Getter
    private Queue<IndicatorValue> history;

    public void update(CandleData candleData){
        update(candleData,IndicatorLogicApplier.FOR_CLOSE);
    }

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

    public void update(Double currentValue,LocalDateTime currentTime){
        Double pastNValue = updatePastValues(currentValue);
        if(pastNValue != null){
            this.value = ((currentValue - pastNValue)/pastNValue)*100.0;
            updateHistory(currentTime,this.value);
        }
    }

    private Double updatePastValues(Double currentValue){
        Double pastValue = null;
        if(this.pastValues.size() > 0 && this.pastValues.size() % (this.period-1) == 0)
            pastValue = this.pastValues.poll();
        this.pastValues.add(currentValue);
        return pastValue;
    }

    private void updateHistory(LocalDateTime currentTime,Double currentValue){
        if(this.history != null){
            if(this.history.size() > 0 && this.history.size() % this.historyCount == 0)
                this.history.poll();
            this.history.add(new IndicatorValue(currentTime,new double[]{currentValue}));
        }
    }

}
