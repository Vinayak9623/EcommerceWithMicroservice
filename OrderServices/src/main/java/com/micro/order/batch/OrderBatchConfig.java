package com.micro.order.batch;

import com.micro.order.model.Order;
import com.micro.order.repository.OrderRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

@Configuration
public class OrderBatchConfig {

    private final OrderRepository orderRepository;

    public OrderBatchConfig(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Bean
    public RepositoryItemReader<Order> reader() {
        RepositoryItemReader<Order> reader = new RepositoryItemReader<>();
        reader.setRepository(orderRepository);
        reader.setMethodName("findAll"); // For demonstration, reading all orders
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }

    @Bean
    public OrderProcessor processor() {
        return new OrderProcessor();
    }

    @Bean
    public FlatFileItemWriter<OrderReportDTO> writer() {
        BeanWrapperFieldExtractor<OrderReportDTO> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"orderId", "userId", "totalAmount", "status", "orderDate"});

        DelimitedLineAggregator<OrderReportDTO> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<OrderReportDTO>()
                .name("orderItemWriter")
                .resource(new FileSystemResource("daily_sales_report.csv"))
                .headerCallback(writer -> writer.write("Order ID,User ID,Total Amount,Status,Order Date"))
                .lineAggregator(lineAggregator)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("csv-step", jobRepository)
                .<Order, OrderReportDTO>chunk(100, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job runJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("dailySalesReportJob", jobRepository)
                .flow(step1(jobRepository, transactionManager))
                .end()
                .build();
    }
}
