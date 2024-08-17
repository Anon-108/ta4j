package ta4jexamples.mytest;


import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
public class EmaV3Indicator  extends CachedIndicator<Num> {
    private final EMAIndicator v2;
    private final EMAIndicator emaV2;

    protected EmaV3Indicator(BarSeries series, EMAIndicator v2, EMAIndicator ema, EMAIndicator emaV2) {
        super(series);
        this.v2 = ema;
        this.emaV2 = emaV2;
    }

    public EmaV3Indicator(Indicator<Num> close, int barCount) {
        super(close);
        this.v2 = new EMAIndicator(close,barCount);
        this.emaV2 = new EMAIndicator(v2,barCount);
    }

    @Override
    protected Num calculate(int index) {
        return DecimalNum.valueOf(2).multipliedBy(v2.getValue(index)).minus(emaV2.getValue(index));
    }

    @Override
    public int getUnstableBars() {
        return 0;
    }


    public EMAIndicator getV2() {
        return v2;
    }

    public EMAIndicator getEmaV2() {
        return emaV2;
    }
}
