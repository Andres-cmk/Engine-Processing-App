package com.andres_cmk.EngineDataProcessingApplication.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // Se ejecuta justo ANTES de empezar el primer registro
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("══════════════════════════════════════════════════════════════");
        log.info("🚀 INICIANDO PROCESAMIENTO MASIVO DE TRANSACCIONES");
        log.info("══════════════════════════════════════════════════════════════");
        log.info("📋 Job Name: {}", jobExecution.getJobInstance().getJobName());
        log.info("🆔 Job Instance ID: {}", jobExecution.getJobInstance().getId());
        log.info("🆔 Job Execution ID: {}", jobExecution.getId());
        log.info("🕒 Hora de inicio: {}", LocalDateTime.now().format(FORMATTER));
        log.info("══════════════════════════════════════════════════════════════");
    }

    // Se ejecuta DESPUÉS del último registro
    @Override
    public void afterJob(JobExecution jobExecution) {
        LocalDateTime inicio = jobExecution.getStartTime();
        LocalDateTime fin = jobExecution.getEndTime();

        if (inicio == null || fin == null) {
            log.error("❌ Error: No se pudo obtener el tiempo de inicio o fin del Job");
            return;
        }

        Duration duracion = Duration.between(inicio, fin);

        long horas = duracion.toHours();
        long minutos = duracion.toMinutesPart();
        long segundos = duracion.toSecondsPart();
        long millis = duracion.toMillisPart();

        log.info("══════════════════════════════════════════════════════════════");
        log.info("✅ ¡TRABAJO COMPLETADO EXITOSAMENTE!");
        log.info("══════════════════════════════════════════════════════════════");
        log.info("📊 ESTADÍSTICAS DEL JOB:");
        log.info("   ├─ Status: {}", jobExecution.getStatus());
        log.info("   ├─ Exit Status: {}", jobExecution.getExitStatus().getExitCode());
        log.info("   ├─ Inicio: {}", inicio.format(FORMATTER));
        log.info("   └─ Fin: {}", fin.format(FORMATTER));
        log.info("──────────────────────────────────────────────────────────────");

        // Mostrar tiempo en formato legible
        if (horas > 0) {
            log.info("⏱️  TIEMPO TOTAL: {} h {} min {} s {} ms", horas, minutos, segundos, millis);
        } else if (minutos > 0) {
            log.info("⏱️  TIEMPO TOTAL: {} min {} s {} ms", minutos, segundos, millis);
        } else if (segundos > 0) {
            log.info("⏱️  TIEMPO TOTAL: {} s {} ms", segundos, millis);
        } else {
            log.info("⏱️  TIEMPO TOTAL: {} ms", millis);
        }

        // Mostrar también en milisegundos totales para benchmarking
        log.info("⏱️  TIEMPO TOTAL (ms): {} ms", duracion.toMillis());

        log.info("──────────────────────────────────────────────────────────────");
        log.info("📈 ESTADÍSTICAS POR STEP:");

        jobExecution.getStepExecutions().forEach(stepExecution -> {
            if (stepExecution.getStartTime() != null && stepExecution.getEndTime() != null) {
                Duration stepDuration = Duration.between(stepExecution.getStartTime(), stepExecution.getEndTime());

                log.info("   🔹 Step: {}", stepExecution.getStepName());
                log.info("      ├─ Registros leídos: {}", stepExecution.getReadCount());
                log.info("      ├─ Registros escritos: {}", stepExecution.getWriteCount());
                log.info("      ├─ Registros filtrados: {}", stepExecution.getFilterCount());
                log.info("      ├─ Registros con error: {}", stepExecution.getSkipCount());
                log.info("      ├─ Tiempo del step: {} ms", stepDuration.toMillis());

                if (stepExecution.getReadCount() > 0) {
                    double avgTimePerRecord = (double) stepDuration.toMillis() / stepExecution.getReadCount();
                    log.info("      ├─ Tiempo promedio por registro: {} ms", String.format("%.3f", avgTimePerRecord));
                    log.info("      └─ Throughput: {} registros/segundo",
                        String.format("%.2f", 1000.0 / avgTimePerRecord));
                }

                if (!stepExecution.getFailureExceptions().isEmpty()) {
                    log.error("      ❌ Errores encontrados:");
                    stepExecution.getFailureExceptions().forEach(ex ->
                        log.error("         • {}", ex.getMessage()));
                }
            }
        });

        log.info("🎉 Procesamiento completado con éxito!");
    }
}
