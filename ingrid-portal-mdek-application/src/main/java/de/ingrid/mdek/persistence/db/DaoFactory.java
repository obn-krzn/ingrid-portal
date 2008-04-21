package de.ingrid.mdek.persistence.db;

import org.hibernate.SessionFactory;

import de.ingrid.mdek.persistence.db.model.HelpMessage;
import de.ingrid.mdek.services.persistence.db.GenericHibernateDao;
import de.ingrid.mdek.services.persistence.db.IDaoFactory;
import de.ingrid.mdek.services.persistence.db.IEntity;
import de.ingrid.mdek.services.persistence.db.IGenericDao;

public class DaoFactory implements IDaoFactory {

    private final SessionFactory _sessionFactory;

    DaoFactory(SessionFactory sessionFactory) {
        _sessionFactory = sessionFactory;
    }

    public IGenericDao<IEntity> getDao(Class clazz) {
		IGenericDao dao = null;

		if (clazz.isAssignableFrom(HelpMessage.class)) {
			dao = new GenericHibernateDao<HelpMessage>(_sessionFactory, HelpMessage.class);
//		} else if (clazz.isAssignableFrom(SearchtermObj.class)) {
//			dao = new GenericHibernateDao<SearchtermObj>(_sessionFactory, SearchtermObj.class);
		} else {
			throw new IllegalArgumentException("Unsupported class: " + clazz.getName());
		}

        return dao;
    }
}
