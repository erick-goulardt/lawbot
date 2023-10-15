package com.ifsul.lawbot.domain.processo;

import com.ifsul.lawbot.domain.advogado.Advogado;
import com.ifsul.lawbot.domain.cliente.Cliente;
import jakarta.persistence.*;

@Entity
@Table(name = "processos")
public class Processo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "advogado_id")
    private Advogado advogado;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
