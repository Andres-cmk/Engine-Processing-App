package com.andres_cmk.EngineDataProcessingApplication.domain.dto;

public record TransactionDTO(
        String idTransaccion,
        String fecha,
        String monto,
        String moneda,
        String comercio,
        String tipo,
        String cuentaOrigen,
        String bancoDestino,
        String nameResponsable

) {
}
