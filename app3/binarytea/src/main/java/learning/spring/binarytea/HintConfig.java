package learning.spring.binarytea;

import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.impl.DefaultJwtParserBuilder;
import learning.spring.binarytea.support.MoneyType;
import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.ResourceHint;
import org.springframework.nativex.hint.ResourcesHints;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.nativex.hint.TypeHints;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@TypeHints({
//        @TypeHint(types = ServiceLoader.Provider.class),
//        @TypeHint(typeNames = "java.util.ServiceLoader$ProviderImpl")
        @TypeHint(types = MoneyType.class),
        // JWT
        @TypeHint(types = DefaultJwtParserBuilder.class),
        @TypeHint(types = DefaultJwtBuilder.class),
        @TypeHint(types = DefaultClaims.class),
        @TypeHint(types = DefaultHeader.class),
        @TypeHint(types = DefaultJwsHeader.class),
        // Joda Money
        @TypeHint(typeNames = "org.joda.money.DefaultCurrencyUnitDataProvider"),
        // Spring
        @TypeHint(types = AuthenticationManagerBuilder.class, access = TypeAccess.DECLARED_FIELDS)
//        @TypeHint(typeNames = "org.springframework.cache.interceptor.CacheExpressionRootObject"),
//        @TypeHint(typeNames = "org.springframework.core.annotation.SynthesizedAnnotation")
})
@ResourcesHints({
        @ResourceHint(patterns = "org/joda/money/CurrencyData.csv"),
        @ResourceHint(patterns = "org/joda/money/CountryData.csv"),
})
public class HintConfig {
}
