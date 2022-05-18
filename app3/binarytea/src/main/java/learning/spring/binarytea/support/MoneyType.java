package learning.spring.binarytea.support;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class MoneyType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[] { Types.BIGINT };
    }

    @Override
    public Class returnedClass() {
        return Money.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if ((x == null) && (y == null)) {
            return true;
        }
        if ((x == null) || (y == null)) {
            return false;
        }
        if (x == y) {
            return true;
        }
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        assert (x != null);
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session,
                              Object owner) throws HibernateException, SQLException {
        Long val = rs.getLong(names[0]);
        return Money.ofMinor(CurrencyUnit.of("CNY"), val);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index,
                            SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (null == value) {
            st.setNull(index, Types.BIGINT);
        } else {
            st.setLong(index, ((Money) value).getAmountMinorLong());
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return null;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return null;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return null;
    }
}
