/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controle.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import controle.Movimentacao;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lucas
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws PreexistingEntityException, Exception {
        if (cliente.getMovimentacaoCollection() == null) {
            cliente.setMovimentacaoCollection(new ArrayList<Movimentacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Movimentacao> attachedMovimentacaoCollection = new ArrayList<Movimentacao>();
            for (Movimentacao movimentacaoCollectionMovimentacaoToAttach : cliente.getMovimentacaoCollection()) {
                movimentacaoCollectionMovimentacaoToAttach = em.getReference(movimentacaoCollectionMovimentacaoToAttach.getClass(), movimentacaoCollectionMovimentacaoToAttach.getId());
                attachedMovimentacaoCollection.add(movimentacaoCollectionMovimentacaoToAttach);
            }
            cliente.setMovimentacaoCollection(attachedMovimentacaoCollection);
            em.persist(cliente);
            for (Movimentacao movimentacaoCollectionMovimentacao : cliente.getMovimentacaoCollection()) {
                Cliente oldIdClienteOfMovimentacaoCollectionMovimentacao = movimentacaoCollectionMovimentacao.getIdCliente();
                movimentacaoCollectionMovimentacao.setIdCliente(cliente);
                movimentacaoCollectionMovimentacao = em.merge(movimentacaoCollectionMovimentacao);
                if (oldIdClienteOfMovimentacaoCollectionMovimentacao != null) {
                    oldIdClienteOfMovimentacaoCollectionMovimentacao.getMovimentacaoCollection().remove(movimentacaoCollectionMovimentacao);
                    oldIdClienteOfMovimentacaoCollectionMovimentacao = em.merge(oldIdClienteOfMovimentacaoCollectionMovimentacao);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCliente(cliente.getId()) != null) {
                throw new PreexistingEntityException("Cliente " + cliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getId());
            Collection<Movimentacao> movimentacaoCollectionOld = persistentCliente.getMovimentacaoCollection();
            Collection<Movimentacao> movimentacaoCollectionNew = cliente.getMovimentacaoCollection();
            List<String> illegalOrphanMessages = null;
            for (Movimentacao movimentacaoCollectionOldMovimentacao : movimentacaoCollectionOld) {
                if (!movimentacaoCollectionNew.contains(movimentacaoCollectionOldMovimentacao)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Movimentacao " + movimentacaoCollectionOldMovimentacao + " since its idCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Movimentacao> attachedMovimentacaoCollectionNew = new ArrayList<Movimentacao>();
            for (Movimentacao movimentacaoCollectionNewMovimentacaoToAttach : movimentacaoCollectionNew) {
                movimentacaoCollectionNewMovimentacaoToAttach = em.getReference(movimentacaoCollectionNewMovimentacaoToAttach.getClass(), movimentacaoCollectionNewMovimentacaoToAttach.getId());
                attachedMovimentacaoCollectionNew.add(movimentacaoCollectionNewMovimentacaoToAttach);
            }
            movimentacaoCollectionNew = attachedMovimentacaoCollectionNew;
            cliente.setMovimentacaoCollection(movimentacaoCollectionNew);
            cliente = em.merge(cliente);
            for (Movimentacao movimentacaoCollectionNewMovimentacao : movimentacaoCollectionNew) {
                if (!movimentacaoCollectionOld.contains(movimentacaoCollectionNewMovimentacao)) {
                    Cliente oldIdClienteOfMovimentacaoCollectionNewMovimentacao = movimentacaoCollectionNewMovimentacao.getIdCliente();
                    movimentacaoCollectionNewMovimentacao.setIdCliente(cliente);
                    movimentacaoCollectionNewMovimentacao = em.merge(movimentacaoCollectionNewMovimentacao);
                    if (oldIdClienteOfMovimentacaoCollectionNewMovimentacao != null && !oldIdClienteOfMovimentacaoCollectionNewMovimentacao.equals(cliente)) {
                        oldIdClienteOfMovimentacaoCollectionNewMovimentacao.getMovimentacaoCollection().remove(movimentacaoCollectionNewMovimentacao);
                        oldIdClienteOfMovimentacaoCollectionNewMovimentacao = em.merge(oldIdClienteOfMovimentacaoCollectionNewMovimentacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cliente.getId();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Movimentacao> movimentacaoCollectionOrphanCheck = cliente.getMovimentacaoCollection();
            for (Movimentacao movimentacaoCollectionOrphanCheckMovimentacao : movimentacaoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Movimentacao " + movimentacaoCollectionOrphanCheckMovimentacao + " in its movimentacaoCollection field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public Cliente findCPF(String cpf) {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Cliente.findByCpf", Cliente.class).setParameter("cpf", cpf).getSingleResult();
        } 
        catch (Exception e) {
            return null;
        } 
        finally {
            em.close();
        }
    }    
}
