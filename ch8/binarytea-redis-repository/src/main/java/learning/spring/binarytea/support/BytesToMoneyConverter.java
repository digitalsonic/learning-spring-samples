package learning.spring.binarytea.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@ReadingConverter
public class BytesToMoneyConverter implements Converter<byte[], Money> {
    private Jackson2JsonRedisSerializer<Money> serializer;

    public BytesToMoneyConverter(ObjectMapper objectMapper) {
        serializer = new Jackson2JsonRedisSerializer<Money>(Money.class);
        serializer.setObjectMapper(objectMapper);
    }

    @Override
    public Money convert(byte[] source) {
        return serializer.deserialize(source);
    }
}
