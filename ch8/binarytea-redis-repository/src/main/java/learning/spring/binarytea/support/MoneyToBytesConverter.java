package learning.spring.binarytea.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@WritingConverter
public class MoneyToBytesConverter implements Converter<Money, byte[]> {
    private Jackson2JsonRedisSerializer<Money> serializer;

    public MoneyToBytesConverter(ObjectMapper objectMapper) {
        serializer = new Jackson2JsonRedisSerializer<Money>(Money.class);
        serializer.setObjectMapper(objectMapper);
    }

    @Override
    public byte[] convert(Money source) {
        return serializer.serialize(source);
    }
}
