package learning.spring.binarytea.controller.formatter;

import org.apache.commons.lang3.math.NumberUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class MoneyFormatter implements Formatter<Money> {
    @Override
    public Money parse(String text, Locale locale) throws ParseException {
        if (NumberUtils.isParsable(text)) {
            return Money.of(CurrencyUnit.of("CNY"), NumberUtils.createBigDecimal(text));
        }
        throw new ParseException(text, 0);
    }

    @Override
    public String print(Money object, Locale locale) {
        return object.getAmount().toString();
    }
}
