package com.distiya.trading.fxindicator.indicator;

import com.distiya.trading.fxindicator.dto.CandleData;
import com.distiya.trading.fxindicator.dto.IndicatorValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

@NoArgsConstructor
public class IndicatorADX{

    public IndicatorADX(Integer period){
        this(period,2);
    }

    public IndicatorADX(Integer period,Integer historyCount){
        this.period = period;
        this.historyCount = historyCount;
        if(this.historyCount > 0)
            this.history = new PriorityQueue<>(historyCount, Comparator.comparing(IndicatorValue::getTime));
        this.adxEMA = new IndicatorEMA(period,0);
        this.plusDIEMA = new IndicatorEMA(period,0);
        this.minusDIEMA = new IndicatorEMA(period,0);
        this.averageTrueRange = new IndicatorEMA(period,0);
    }

    @Setter
    private Integer period;
    @Setter
    private Integer historyCount;
    @Getter
    private Queue<IndicatorValue> history;

    private CandleData previousCandle;
    private CandleData currentCandle;
    private IndicatorEMA adxEMA;
    private IndicatorEMA plusDIEMA;
    private IndicatorEMA minusDIEMA;
    private IndicatorEMA averageTrueRange;
    private Double plusDI = 0.0d;
    private Double minusDI = 0.0d;
    private Double adx = 0.0d;

    public void update(CandleData currentPrice) {
        this.previousCandle = this.currentCandle;
        this.currentCandle = currentPrice;
        if(this.previousCandle != null && this.currentCandle != null){
            // trueRange = max(high,previous close) - min(low,previous close)
            double trueRange = Math.max(this.currentCandle.getHigh(),this.previousCandle.getClose()) - Math.min(this.currentCandle.getLow(),this.previousCandle.getClose());
            this.averageTrueRange.update(trueRange,this.currentCandle.getTime());
            if(this.averageTrueRange.getEmaPeriodProgressCount() >= this.period){
                calculateDIEMA(this.averageTrueRange.getValue());
            }
        }
    }

    public double getADX(){
        if(this.adxEMA.getEmaPeriodProgressCount() < this.period)
            return -300.0d;
        else
            return adx;
    }

    public double getPlusDI(){
        if(this.adxEMA.getEmaPeriodProgressCount() < this.period)
            return -300.0d;
        else
            return plusDI;
    }

    public double getMinusDI(){
        if(this.adxEMA.getEmaPeriodProgressCount() < this.period)
            return -300.0d;
        else
            return minusDI;
    }

    private void calculateDIEMA(double atr){
        double dm_plus;
        double dm_minus;
        double upm = this.currentCandle.getHigh() - this.previousCandle.getHigh();
        double dwm = this.previousCandle.getLow() - this.currentCandle.getLow();
        dm_plus = (upm > dwm && upm > 0) ? upm/atr : 0.0d;
        dm_minus = (dwm > upm && dwm > 0) ? dwm/atr : 0.0d;
        this.plusDIEMA.update(dm_plus,this.currentCandle.getTime());
        this.minusDIEMA.update(dm_minus,this.currentCandle.getTime());
        if(this.plusDIEMA.getEmaPeriodProgressCount() >= this.period){
            calculateDI();
        }
    }

    private void calculateDI(){
        this.plusDI = plusDIEMA.getValue() * 100.0;
        this.minusDI = minusDIEMA.getValue() * 100.0;
        this.adxEMA.update(Math.abs((plusDI-minusDI)/(plusDI+minusDI)),this.currentCandle.getTime());
        if(this.adxEMA.getEmaPeriodProgressCount() >= this.period){
            this.adx = this.adxEMA.getValue() * 100.0;
            updateHistory();
        }
    }

    private void updateHistory(){
        if(this.history != null){
            if(this.history.size() > 0 && this.history.size() % this.historyCount == 0)
                this.history.poll();
            this.history.add(new IndicatorValue(this.currentCandle.getTime(),new double[]{this.adx,this.plusDI,this.minusDI}));
        }
    }
}
