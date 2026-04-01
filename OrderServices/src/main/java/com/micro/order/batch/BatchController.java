package com.micro.order.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/order/batch")
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @PostMapping("/generate-report")
    public ResponseEntity<?> generateReport() {
        try {
            // 1. Run the Spring Batch Job (Synchronous by default)
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("run.start", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);

            // 2. Locate the generated CSV file
            File file = new File("daily_sales_report.csv");
            if (!file.exists()) {
                return ResponseEntity.internalServerError().body("Report generation failed: File not found.");
            }

            // 3. Prepare the file as a downloadable resource
            Resource resource = new FileSystemResource(file);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Batch job failed: " + e.getMessage());
        }
    }
}
