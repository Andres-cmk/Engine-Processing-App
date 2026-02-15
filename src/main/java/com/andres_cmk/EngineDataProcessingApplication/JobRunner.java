package com.andres_cmk.EngineDataProcessingApplication;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job job;

    public JobRunner(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("⚡ INICIANDO EJECUCIÓN MANUAL CON ID ÚNICO ⚡");

        // Aquí está el truco: Agregamos el tiempo actual como parámetro
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("inicio", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(job, jobParameters);
    }
}