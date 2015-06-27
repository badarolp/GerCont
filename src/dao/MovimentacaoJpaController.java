/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import controle.Cliente;
import controle.Movimentacao;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lucas
 */
public class MovimentacaoJpaController implements Serializable {

    public MovimentacaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movimentacao movimentacao) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = movimentacao.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getId());
                movimentacao.setIdCliente(idCliente);
            }
            em.persist(movimentacao);
            if (idCliente != null) {
                idCliente.getMovimentacaoCollection().add(movimentacao);
                idCliente = em.merge(idCliente);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMovimentacao(movimentacao.getId()) != null) {
                throw new PreexistingEntityException("Movimentacao " + movimentacao + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Movimentacao movimentacao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movimentacao persistentMovimentacao = em.find(Movimentacao.class, movimentacao.getId());
            Cliente idClienteOld = persistentMovimentacao.getIdCliente();
            Cliente idClienteNew = movimentacao.getIdCliente();
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getId());
                movimentacao.setIdCliente(idClienteNew);
            }
            movimentacao = em.merge(movimentacao);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getMovimentacaoCollection().remove(movimentacao);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getMovimentacaoCollection().add(movimentacao);
                idClienteNew = em.merge(idClienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = movimentacao.getId();
                if (findMovimentacao(id) == null) {
                    throw new NonexistentEntityException("The movimentacao with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movimentacao movimentacao;
            try {
                movimentacao = em.getReference(Movimentacao.class, id);
                movimentacao.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimentacao with id " + id + " no longer exists.", enfe);
            }
            Cliente idCliente = movimentacao.getIdCliente();
            if (idCliente != null) {
                idCliente.getMovimentacaoCollection().remove(movimentacao);
                idCliente = em.merge(idCliente);
            }
            em.remove(movimentacao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Movimentacao> findMovimentacaoEntities() {
        return findMovimentacaoEntities(true, -1, -1);
    }

    public List<Movimentacao> findMovimentacaoEntities(int maxResults, int firstResult) {
        return findMovimentacaoEntities(false, maxResults, firstResult);
    }

    private List<Movimentacao> findMovimentacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Movimentacao.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Movimentacao findMovimentacao(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movimentacao.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimentacaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Movimentacao> rt = cq.from(Movimentacao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
