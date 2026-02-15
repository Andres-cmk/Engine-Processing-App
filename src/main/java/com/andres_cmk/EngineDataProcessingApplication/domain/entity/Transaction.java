package com.andres_cmk.EngineDataProcessingApplication.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Transaction {
    @Id
    @Column(name = "id_transaccion", nullable = false,  unique = true)
    private String id_transaccion;

    @Column(name = "fecha_transaccion")
    private LocalDateTime fecha;

    @Column(name = "monto", precision = 10, scale = 2)
    private BigDecimal valor;

    private String moneda;

    @Column(name = "tipo_transferencia")
    private String tipo;

    @Column(name = "cuenta_origen")
    private String cuentaOrigen;

    @Column(name = "banco_destino")
    private String bancoDestino;

    @Column
    private LocalDateTime createdAt;

    @Column(name = "responsable", nullable = false)
    private String nameResponsable;

    @PrePersist
    protected void onCreate(){createdAt = LocalDateTime.now();}
}
