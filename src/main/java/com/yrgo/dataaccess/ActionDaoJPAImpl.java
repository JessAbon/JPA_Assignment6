package com.yrgo.dataaccess;

import com.yrgo.domain.Action;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ActionDaoJPAImpl implements ActionDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Action newAction) {
        System.out.println("using jpa");
        em.persist(newAction);
    }

    @Override
    public List<Action> getIncompleteActions(String userId) {
        return em.createQuery("SELECT ACTION_ID, DETAILS, COMPLETE, OWNING_USER, REQUIRED_BY FROM ACTION WHERE OWNING_USER = :OWNING_USER AND COMPLETE = :COMPLETE", Action.class)
                .setParameter("OWNING_USER", userId)
                .setParameter("COMPLETE", false)
                .getResultList();
    }

    @Override
    public void update(Action actionToUpdate) throws RecordNotFoundException {
        Action existingAction = em.find(Action.class, actionToUpdate.getActionId());

        existingAction.setDetails(actionToUpdate.getDetails());
        existingAction.setComplete(actionToUpdate.isComplete());
        existingAction.setOwningUser(actionToUpdate.getOwningUser());
        existingAction.setRequiredBy(actionToUpdate.getRequiredBy());

        // synkronisera ändringar till databasen
        em.merge(existingAction);
    }

    @Override
    public void delete(Action oldAction) throws RecordNotFoundException {
        Action action = em.find(Action.class, oldAction.getActionId());
        em.remove(action);
    }
}
