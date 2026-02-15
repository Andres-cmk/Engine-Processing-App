package com.andres_cmk.EngineDataProcessingApplication.Config;

import com.andres_cmk.EngineDataProcessingApplication.domain.dto.TransactionDTO;
import com.andres_cmk.EngineDataProcessingApplication.domain.entity.Transaction;
import com.andres_cmk.EngineDataProcessingApplication.listener.JobCompletionNotificationListener;
import com.andres_cmk.EngineDataProcessingApplication.processor.TransactionProcessor;
import com.andres_cmk.EngineDataProcessingApplication.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;


@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final TransactionRepository transactionRepository;
    private final JobCompletionNotificationListener JobCompletionNotificationListener;



    public FlatFileItemReader<TransactionDTO> reader() {
        return new FlatFileItemReaderBuilder<TransactionDTO>()
                .name("TransaccionItemReader")
                .resource(new ClassPathResource("datos_bancarios.csv"))
                .delimited()
                .names("idTransaccion", "fecha", "monto", "moneda", "comercio", "tipo", "cuentaOrigen", "bancoDestino", "nameResponsable")
                .linesToSkip(1)
                .targetType(TransactionDTO.class)
                .build();
    }

    @Bean
    public TransactionProcessor processor() {
        return new TransactionProcessor();
    }

    @Bean
    public RepositoryItemWriter<Transaction> writer() {
        return new RepositoryItemWriterBuilder<Transaction>()
                .repository(transactionRepository)
                .methodName("save")
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<TransactionDTO, Transaction>chunk(50_000, transactionManager)
                .reader(readerBuilder())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job importTransaccionJob() {
        return new JobBuilder("importTransactionJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(JobCompletionNotificationListener)
                .start(step1())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("JobExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public SynchronizedItemStreamReader<TransactionDTO> readerBuilder() {
        return new SynchronizedItemStreamReaderBuilder<TransactionDTO>()
                .delegate(reader())
                .build();
    }

}
