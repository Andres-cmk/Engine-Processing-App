package com.andres_cmk.EngineDataProcessingApplication.processor;

import com.andres_cmk.EngineDataProcessingApplication.domain.dto.TransactionDTO;
import com.andres_cmk.EngineDataProcessingApplication.domain.entity.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TransactionProcessor implements ItemProcessor<TransactionDTO, Transaction> {


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Transaction process(TransactionDTO item) {

        BigDecimal monto = new BigDecimal(item.monto());
        LocalDateTime fecha = LocalDateTime.parse(item.fecha(), FORMATTER);

        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("⚠️ Transacción rechazada - Monto inválido: {} para ID {}", item.monto(), item.idTransaccion());
            return null;
        }

        final Transaction transaccion = new Transaction();
        transaccion.setId_transaccion(item.idTransaccion());
        transaccion.setFecha(fecha);
        transaccion.setValor(monto);
        transaccion.setMoneda(item.moneda());
        transaccion.setTipo(item.tipo());
        transaccion.setCuentaOrigen(item.cuentaOrigen());
        transaccion.setBancoDestino(item.bancoDestino());
        transaccion.setNameResponsable(item.nameResponsable());

        log.debug("✅ Transacción procesada correctamente: {}", item.idTransaccion());

        return  transaccion;
    }
}
