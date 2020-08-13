package com.distiya.trading.fxindicator;

import com.distiya.trading.fxindicator.constant.IndicatorLogicApplier;
import com.distiya.trading.fxindicator.dto.CandleData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest(classes = {TestConfig.class})
@Slf4j
public class TestIndicators {

    @Test
    public void testIndicatorEMA(){
        double values[] = new double[]{3345.6,3324.6,3212.5,3114.5,3456.4,3214.6,3678.2,3114.6,3254.3,3614.2,3294.1};
        IndicatorEMA indicatorEMA = new IndicatorEMA(5);
        int i;
        for(i=0;i<values.length;i++){
            CandleData data = new CandleData();
            data.setTime(LocalDateTime.now().plusSeconds(i*5));
            data.setClose(values[i]);
            indicatorEMA.update(data);
            log.info("============ EMA Updating Started =================");
            log.info("EMA Value : {}",indicatorEMA.getValue());
            indicatorEMA.getHistory().stream().forEach(iv->log.info("History Time : {}, History Value : {}",iv.getTime(),iv.getValues()[0]));
            log.info("============ EMA Updating Ended =================");
        }
    }

    @Test
    public void testIndicatorCrossEMA(){
        double values[] = new double[]{3345.6,3324.6,3212.5,3114.5,3456.4,3214.6,3678.2,3114.6,3254.3,3614.2,3294.1};
        IndicatorCrossEMA indicatorCrossEMA = new IndicatorCrossEMA(5,3);
        int i;
        for(i=0;i<values.length;i++){
            CandleData data = new CandleData();
            data.setTime(LocalDateTime.now().plusSeconds(i*5));
            data.setClose(values[i]);
            indicatorCrossEMA.update(data);
            log.info("============ Cross EMA Updating Started =================");
            log.info("EMA slow : {}, EMA fast : {}",indicatorCrossEMA.getSlowEMA(),indicatorCrossEMA.getFastEMA());
            indicatorCrossEMA.getHistory().stream().forEach(iv->log.info("History Time : {}, Slow EMA : {}, Fast EMA : {}",iv.getTime(),iv.getValues()[0],iv.getValues()[1]));
            log.info("============ Cross EMA Updating Ended =================");
        }
    }

    @Test
    public void testIndicatorADX(){
        double closeValues[] = new double[]{3345.6,3324.6,3212.5,3114.5,3456.4,3214.6,3678.2,3114.6,3254.3,3614.2,3294.1};
        double highValues[] = new double[]{3348.6,3329.6,3217.5,3119.5,3459.4,3217.6,3685.2,3119.6,3259.3,3619.2,3299.1};
        double lowValues[] = new double[]{3343.6,3321.6,3204.5,3110.5,3450.4,3210.6,3674.2,3111.6,3252.3,3611.2,3290.1};
        IndicatorADX indicatorAdx = new IndicatorADX(3);
        int i;
        for(i=0;i<closeValues.length;i++){
            CandleData data = new CandleData();
            data.setTime(LocalDateTime.now().plusSeconds(i*5));
            data.setClose(closeValues[i]);
            data.setHigh(highValues[i]);
            data.setLow(lowValues[i]);
            indicatorAdx.update(data);
            log.info("============ ADX Updating Started =================");
            log.info("ADX : {}, DI+ : {}, DI- : {}",indicatorAdx.getADX(),indicatorAdx.getPlusDI(),indicatorAdx.getMinusDI());
            indicatorAdx.getHistory().stream().forEach(iv->log.info("History Time : {}, ADX : {}, DI+ : {}, DI- : {}",iv.getTime(),iv.getValues()[0],iv.getValues()[1],iv.getValues()[2]));
            log.info("============ ADX Updating Ended =================");
        }
    }

    @Test
    public void testIndicatorROC(){
        double closeValues[] = new double[]{3345.6,3324.6,3212.5,3114.5,3456.4,3214.6,3678.2,3114.6,3254.3,3614.2,3294.1};
        double highValues[] = new double[]{3348.6,3329.6,3217.5,3119.5,3459.4,3217.6,3685.2,3119.6,3259.3,3619.2,3299.1};
        double lowValues[] = new double[]{3343.6,3321.6,3204.5,3110.5,3450.4,3210.6,3674.2,3111.6,3252.3,3611.2,3290.1};
        IndicatorROC indicatorRoc = new IndicatorROC(5,IndicatorLogicApplier.FOR_CLOSE);
        int i;
        for(i=0;i<closeValues.length;i++){
            CandleData data = new CandleData();
            data.setTime(LocalDateTime.now().plusSeconds(i*5));
            data.setClose(closeValues[i]);
            data.setHigh(highValues[i]);
            data.setLow(lowValues[i]);
            indicatorRoc.update(data);
            log.info("============ ROC Updating Started =================");
            log.info("ROC : {}",indicatorRoc.getValue());
            indicatorRoc.getHistory().stream().forEach(iv->log.info("History Time : {}, ROC : {}",iv.getTime(),iv.getValues()[0]));
            log.info("============ ROC Updating Ended =================");
        }
    }

    @Test
    public void testIndicatorSMA(){
        double closeValues[] = new double[]{3345.6,3324.6,3212.5,3114.5,3456.4,3214.6,3678.2,3114.6,3254.3,3614.2,3294.1};
        double highValues[] = new double[]{3348.6,3329.6,3217.5,3119.5,3459.4,3217.6,3685.2,3119.6,3259.3,3619.2,3299.1};
        double lowValues[] = new double[]{3343.6,3321.6,3204.5,3110.5,3450.4,3210.6,3674.2,3111.6,3252.3,3611.2,3290.1};
        IndicatorSMA indicatorSMA = new IndicatorSMA(5);
        int i;
        for(i=0;i<closeValues.length;i++){
            CandleData data = new CandleData();
            data.setTime(LocalDateTime.now().plusSeconds(i*5));
            data.setClose(closeValues[i]);
            data.setHigh(highValues[i]);
            data.setLow(lowValues[i]);
            indicatorSMA.update(data);
            log.info("============ SMA Updating Started =================");
            log.info("SMA : {}",indicatorSMA.getValue());
            indicatorSMA.getHistory().stream().forEach(iv->log.info("History Time : {}, SMA : {}",iv.getTime(),iv.getValues()[0]));
            log.info("============ SMA Updating Ended =================");
        }
    }

    @Test
    public void testIndicatorStochastic(){
        double closeValues[] = new double[]{3345.6,3324.6,3212.5,3114.5,3456.4,3214.6,3678.2,3114.6,3254.3,3614.2,3294.1};
        double highValues[] = new double[]{3348.6,3329.6,3217.5,3119.5,3459.4,3217.6,3685.2,3119.6,3259.3,3619.2,3299.1};
        double lowValues[] = new double[]{3343.6,3321.6,3204.5,3110.5,3450.4,3210.6,3674.2,3111.6,3252.3,3611.2,3290.1};
        IndicatorStochastic indicatorStochastic = new IndicatorStochastic(5,3,3);
        int i;
        for(i=0;i<closeValues.length;i++){
            CandleData data = new CandleData();
            data.setTime(LocalDateTime.now().plusSeconds(i*5));
            data.setClose(closeValues[i]);
            data.setHigh(highValues[i]);
            data.setLow(lowValues[i]);
            indicatorStochastic.update(data);
            log.info("============ Stochastic Updating Started =================");
            log.info("KP : {}, DP : {}, DNP : {}",indicatorStochastic.getKP(),indicatorStochastic.getDP(),indicatorStochastic.getDNP());
            indicatorStochastic.getHistory().stream().forEach(iv->log.info("History Time : {}, KP : {}, DP : {}, DNP : {}",iv.getTime(),iv.getValues()[0],iv.getValues()[1],iv.getValues()[2]));
            log.info("============ Stochastic Updating Ended =================");
        }
    }

    @Test
    public void testIndicatorRSI(){
        double closeValues[] = new double[]{3345.6,3324.6,3212.5,3114.5,3456.4,3214.6,3678.2,3114.6,3254.3,3614.2,3294.1};
        double highValues[] = new double[]{3348.6,3329.6,3217.5,3119.5,3459.4,3217.6,3685.2,3119.6,3259.3,3619.2,3299.1};
        double lowValues[] = new double[]{3343.6,3321.6,3204.5,3110.5,3450.4,3210.6,3674.2,3111.6,3252.3,3611.2,3290.1};
        IndicatorRSI indicatorRSI = new IndicatorRSI(3,IndicatorLogicApplier.FOR_CLOSE);
        int i;
        for(i=0;i<closeValues.length;i++){
            CandleData data = new CandleData();
            data.setTime(LocalDateTime.now().plusSeconds(i*5));
            data.setClose(closeValues[i]);
            data.setHigh(highValues[i]);
            data.setLow(lowValues[i]);
            indicatorRSI.update(data);
            log.info("============ Stochastic Updating Started =================");
            log.info("RSI : {}",indicatorRSI.getValue());
            indicatorRSI.getHistory().stream().forEach(iv->log.info("History Time : {}, RSI : {}",iv.getTime(),iv.getValues()[0]));
            log.info("============ Stochastic Updating Ended =================");
        }
    }

    @Test
    public void testIndicatorDiffPercent(){
        double closeValues[] = new double[]{3345.6,3324.6,3212.5,3114.5,3456.4,3214.6,3678.2,3114.6,3254.3,3614.2,3294.1};
        double highValues[] = new double[]{3348.6,3329.6,3217.5,3119.5,3459.4,3217.6,3685.2,3119.6,3259.3,3619.2,3299.1};
        double lowValues[] = new double[]{3343.6,3321.6,3204.5,3110.5,3450.4,3210.6,3674.2,3111.6,3252.3,3611.2,3290.1};
        IndicatorDiffPercent indicatorDiffPercent = new IndicatorDiffPercent(3);
        int i;
        for(i=0;i<closeValues.length;i++){
            CandleData data = new CandleData();
            data.setTime(LocalDateTime.now().plusSeconds(i*5));
            data.setClose(closeValues[i]);
            data.setHigh(highValues[i]);
            data.setLow(lowValues[i]);
            indicatorDiffPercent.update(data.getTime(),data.getHigh(),data.getLow());
            log.info("============ Stochastic Updating Started =================");
            log.info("DiffPercent : {}",indicatorDiffPercent.getValue());
            indicatorDiffPercent.getHistory().stream().forEach(iv->log.info("History Time : {}, DiffPercent : {}",iv.getTime(),iv.getValues()[0]));
            log.info("============ Stochastic Updating Ended =================");
        }
    }

    @Test
    public void testIndicatorCrossEMADiffPercent(){
        double closeValues[] = new double[]{3345.6,3324.6,3212.5,3114.5,3456.4,3214.6,3678.2,3114.6,3254.3,3614.2,3294.1};
        double highValues[] = new double[]{3348.6,3329.6,3217.5,3119.5,3459.4,3217.6,3685.2,3119.6,3259.3,3619.2,3299.1};
        double lowValues[] = new double[]{3343.6,3321.6,3204.5,3110.5,3450.4,3210.6,3674.2,3111.6,3252.3,3611.2,3290.1};
        IndicatorCrossEMADiffPercent indicatorCrossEMADiffPercent = new IndicatorCrossEMADiffPercent(5,3, IndicatorLogicApplier.FOR_CLOSE);
        int i;
        for(i=0;i<closeValues.length;i++){
            CandleData data = new CandleData();
            data.setTime(LocalDateTime.now().plusSeconds(i*5));
            data.setClose(closeValues[i]);
            data.setHigh(highValues[i]);
            data.setLow(lowValues[i]);
            indicatorCrossEMADiffPercent.update(data);
            log.info("============ Stochastic Updating Started =================");
            log.info("Slow EMA DiffPercent : {}, Fast EMA DiffPercent {}",indicatorCrossEMADiffPercent.getSlowEMAPriceDiffPercent(),indicatorCrossEMADiffPercent.getFastEMAPriceDiffPercent());
            indicatorCrossEMADiffPercent.getHistory().stream().forEach(iv->log.info("History Time : {}, Slow DiffPercent : {}, Fast DiffPercent : {}",iv.getTime(),iv.getValues()[0],iv.getValues()[1]));
            log.info("============ Stochastic Updating Ended =================");
        }
    }
}
