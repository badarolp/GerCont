/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import dao.MovimentacaoJpaController;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Persistence;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.swing.JOptionPane;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lucas
 */
@Entity
@Table(name = "movimentacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Movimentacao.findAll", query = "SELECT m FROM Movimentacao m"),
    @NamedQuery(name = "Movimentacao.findById", query = "SELECT m FROM Movimentacao m WHERE m.id = :id"),
    @NamedQuery(name = "Movimentacao.findByData", query = "SELECT m FROM Movimentacao m WHERE m.data = :data"),
    @NamedQuery(name = "Movimentacao.findByObservacao", query = "SELECT m FROM Movimentacao m WHERE m.observacao = :observacao"),
    @NamedQuery(name = "Movimentacao.findByValor", query = "SELECT m FROM Movimentacao m WHERE m.valor = :valor"),
    @NamedQuery(name = "Movimentacao.findByTipo", query = "SELECT m FROM Movimentacao m WHERE m.tipo = :tipo")})
public class Movimentacao implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "Data", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date data;
    @Basic(optional = false)
    @Column(name = "Observacao", nullable = false, length = 500)
    private String observacao;
    @Basic(optional = false)
    @Column(name = "Valor", nullable = false)
    private double valor;    
    @Basic(optional = false)
    @Column(name = "tipo", nullable = false)
    private int tipo;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Cliente idCliente;

    public Movimentacao() {
    }

    public Movimentacao(Long id) {
        this.id = id;
    }

    public Movimentacao(Long id, Date data, String observacao, double valor, int tipo) {
        this.id = id;
        this.data = data;
        this.observacao = observacao;
        this.valor = valor;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        Long oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        Date oldData = this.data;
        this.data = data;
        changeSupport.firePropertyChange("data", oldData, data);
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        String oldObservacao = this.observacao;
        this.observacao = observacao;
        changeSupport.firePropertyChange("observacao", oldObservacao, observacao);
    }
    
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        double oldValor = this.valor;
        this.valor = valor;
        changeSupport.firePropertyChange("valor", oldValor, valor);
    }    

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        int oldTipo = this.tipo;
        this.tipo = tipo;
        changeSupport.firePropertyChange("tipo", oldTipo, tipo);
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        Cliente oldIdCliente = this.idCliente;
        this.idCliente = idCliente;
        changeSupport.firePropertyChange("idCliente", oldIdCliente, idCliente);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movimentacao)) {
            return false;
        }
        Movimentacao other = (Movimentacao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "controle.Movimentacao[ id=" + id + " ]";
    }
    public boolean armazenado() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaApplicationCadastroClientePU");
            MovimentacaoJpaController movimentacaoJpaController = new MovimentacaoJpaController(emf);
            movimentacaoJpaController.create(this);
            JOptionPane.showMessageDialog(null, "Movimentação incluída");
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getCause());
            return false;
        }
    }

    public boolean atualizado() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaApplicationCadastroClientePU");
            MovimentacaoJpaController movimentacaoJpaController = new MovimentacaoJpaController(emf);
            movimentacaoJpaController.edit(this);
            JOptionPane.showMessageDialog(null, "Movimentação atualizada");
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getCause());
            return false;
        }
    }

    public boolean desarmazenado() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaApplicationCadastroClientePU");
            MovimentacaoJpaController movimentacaoJpaController = new MovimentacaoJpaController(emf);
            movimentacaoJpaController.destroy(this.getId());
            JOptionPane.showMessageDialog(null, "Movimentação excluída");
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getCause());
            return false;
        }
    }

    public boolean encontradoId(Long id) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("JavaApplicationCadastroClientePU");
            MovimentacaoJpaController movimentacaoJpaController = new MovimentacaoJpaController(emf);
            Movimentacao movimentacaoAux = movimentacaoJpaController.findMovimentacao(id);
            if (movimentacaoAux != null) {
                this.setId(movimentacaoAux.getId());
                this.setObservacao(movimentacaoAux.getObservacao());
                this.setData(movimentacaoAux.getData());
                this.setValor(movimentacaoAux.getValor());
                this.setTipo(movimentacaoAux.getTipo());
                this.setIdCliente(movimentacaoAux.getIdCliente());
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Movimentação não encontrada");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getCause());
            return false;
        }
    }    

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
