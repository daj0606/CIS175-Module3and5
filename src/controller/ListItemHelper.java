/**
 * @author ${user} - djackson16
 * CIS175 - Spring 2024
 * 2/4/2024
 */
package controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import model.ListItem;


public class ListItemHelper {
	static EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("GameRanks");
	
	public void InsertItem(ListItem li) {
		EntityManager em = emfactory.createEntityManager();
		em.getTransaction().begin();
		em.persist(li);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<ListItem> showAllItems(){
		EntityManager em = emfactory.createEntityManager();
		// was getting "Type safety: The expression of type List needs unchecked conversion to conform to List<ListItem>" so I added "@SuppressWarnings("unchecked")"
		@SuppressWarnings("unchecked")
		List<ListItem> allItems = em.createQuery("SELECT i FROM ListItem i").getResultList();
		return allItems;
		}
	
	public void deleteItem(ListItem toDelete) {
		EntityManager em = emfactory.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<ListItem> typedQuery = em.createQuery("select li from ListItem li where li.game = :selectedGame and li.rank = :selectedRank", ListItem.class);
		
		//Substitute parameter with actual data from the toDelete item
		typedQuery.setParameter("selectedGame", toDelete.getGame());
		typedQuery.setParameter("selectedRank", toDelete.getRank());
		
		//we only want one result
		typedQuery.setMaxResults(1);
		
		//get the result and save it into a new list item
		ListItem result = typedQuery.getSingleResult();
		
		//remove it
		em.remove(result);
		em.getTransaction().commit();
		em.close();
	}

	public ListItem searchForRankById(int idToEdit) {
	EntityManager em = emfactory.createEntityManager();
	em.getTransaction().begin();
	ListItem found = em.find(ListItem.class, idToEdit);
	em.close();
	return found;
	}

	public void updateItem(ListItem toEdit) {
		EntityManager em = emfactory.createEntityManager();
		em.getTransaction().begin();
		em.merge(toEdit);
		em.getTransaction().commit();
		em.close();
		}

	public List<ListItem> searchForRankByGame(String gameName) {
		EntityManager em = emfactory.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<ListItem> typedQuery = em.createQuery("select li from ListItem li where li.game = :selectedGame", ListItem.class);
		typedQuery.setParameter("selectedGame", gameName);
		List<ListItem> foundItems = typedQuery.getResultList();
		em.close();
		return foundItems;
	}

	public List<ListItem> searchForRankByRank(String rankName) {
		EntityManager em = emfactory.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<ListItem> typedQuery = em.createQuery("select li from ListItem li where li.rank = :selectedRank", ListItem.class);
		typedQuery.setParameter("selectedItem", rankName);
		List<ListItem> foundItems = typedQuery.getResultList();
		em.close();
		return foundItems;
		}
	
	public void cleanUp() {
		emfactory.close();
	}
	
}
