package com.jade.microservices.configsvr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigServer
public class ConfigSvrApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigSvrApplication.class, args);
	}

	@Bean
	public MetricWriter getMetricWriter() {
		return new MetricWriter() {
			@Override
			public void increment(Delta<?> delta) {

			}

			@Override
			public void reset(String s) {

			}

			@Override
			public void set(Metric<?> metric) {

			}
		};
	}
}
