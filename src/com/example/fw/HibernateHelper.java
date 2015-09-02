package com.example.fw;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateHelper extends HelperBase {

	public HibernateHelper(ApplicationManager manager) {
	  super(manager);
	}

/*
	public String getUserId(String login) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction trans = session.beginTransaction();
		try {
          return session.createQuery("select id from User where name=?")
        		  .setParameter(0, login).uniqueResult().toString();
		} finally {
          trans.commit();
		}
	}
*/
	
	public String getUserId(String login) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
        return session.createQuery("select id from User where login=?")
        		.setParameter(0, login).uniqueResult().toString();
	}
	
}
