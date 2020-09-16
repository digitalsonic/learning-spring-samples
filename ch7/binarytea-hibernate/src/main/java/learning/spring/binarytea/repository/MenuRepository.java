package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.MenuItem;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class MenuRepository extends HibernateDaoSupport {
    public MenuRepository(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    public long countMenuItems() {
        return getSessionFactory().getCurrentSession()
                .createQuery("select count(t) from MenuItem t", Long.class)
                .getSingleResult();
    }

    public List<MenuItem> queryAllItems() {
        return getHibernateTemplate().loadAll(MenuItem.class);
    }

    public MenuItem queryForItem(Long id) {
        return getHibernateTemplate().get(MenuItem.class, id);
    }

    public void insertItem(MenuItem item) {
        getHibernateTemplate().save(item);
    }

    public void updateItem(MenuItem item) {
        getHibernateTemplate().update(item);
    }

    public void deleteItem(Long id) {
        MenuItem item = getHibernateTemplate().get(MenuItem.class, id);
        if (item != null) {
            getHibernateTemplate().delete(item);
        }
    }
}
