package com.distiya.trading.fxindicator.indicator;

import com.distiya.trading.fxindicator.dto.CandleData;
import com.distiya.trading.fxindicator.dto.IndicatorValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

@Slf4j
public class IndicatorStochastic{

    private Integer kPeriod = 10;
    private Integer dPeriod = 6;
    private Integer dnPeriod = 6;
    private Integer historyCount;
    @Getter
    private Queue<IndicatorValue> history;
    Queue<CandleData> candleHistory = new LinkedList<>();
    private Double kP = 0.0;
    private IndicatorSMA dpSMA;
    private IndicatorSMA dnpSMA;

    public IndicatorStochastic(Integer kPeriod, Integer dPeriod, Integer dnPeriod){
        this(kPeriod,dPeriod,dnPeriod,2);
    }

    public IndicatorStochastic(Integer kPeriod, Integer dPeriod, Integer dnPeriod,Integer historyCount){
        this.kPeriod = kPeriod;
        this.dPeriod = dPeriod;
        this.dnPeriod = dnPeriod;
        this.historyCount = historyCount;
        if(this.historyCount > 0)
            this.history = new PriorityQueue<>(historyCount, Comparator.comparing(IndicatorValue::getTime));
        this.dpSMA = new IndicatorSMA(this.dPeriod,0);
        this.dnpSMA = new IndicatorSMA(this.dnPeriod,0);
    }

    public void update(CandleData currentPrice) {
        updateCandleHistory(currentPrice);
        if(candleHistory.size() == kPeriod){
            kP = getCurrentStochastic(currentPrice.getClose(),calculateCurrentHigh(),calculateCurrentLow());
            dpSMA.update(kP,currentPrice.getTime());
            if(dpSMA.getSmaPeriodProgressCount() >= dPeriod)
                dnpSMA.update(dpSMA.getValue(),currentPrice.getTime());
            if(dnpSMA.getSmaPeriodProgressCount() >= dnPeriod)
                updateHistory(currentPrice.getTime());
        }
    }

    public Double getKP(){
        if(this.dnpSMA.getSmaPeriodProgressCount() < this.dnPeriod)
            return -300.0;
        else
            return this.kP;
    }

    public Double getDP(){
        if(this.dnpSMA.getSmaPeriodProgressCount() < this.dnPeriod)
            return -300.0;
        else
            return this.dpSMA.getValue();
    }

    public Double getDNP(){
        if(this.dnpSMA.getSmaPeriodProgressCount() < this.dnPeriod)
            return -300.0;
        else
            return this.dnpSMA.getValue();
    }

    private double calculateCurrentHigh(){
        return candleHistory.stream().mapToDouble(pc->pc.getHigh()).summaryStatistics().getMax();
    }

    private double calculateCurrentLow(){
        return candleHistory.stream().mapToDouble(pc->pc.getLow()).summaryStatistics().getMin();
    }

    private double getCurrentStochastic(double close,double high, double low){
        return (close - low)/(high - low)*100;
    }

    private void updateCandleHistory(CandleData currentPrice){
        if(this.candleHistory != null){
            if(this.candleHistory.size() > 0 && this.candleHistory.size() % this.kPeriod == 0)
                this.candleHistory.poll();
            this.candleHistory.add(currentPrice);
        }
    }

    private void updateHistory(LocalDateTime time){
        if(this.history != null){
            if(this.history.size() > 0 && this.history.size() % this.historyCount == 0)
                this.history.poll();
            this.history.add(new IndicatorValue(time,new double[]{this.kP,this.dpSMA.getValue(),this.dnpSMA.getValue()}));
        }
    }
}
