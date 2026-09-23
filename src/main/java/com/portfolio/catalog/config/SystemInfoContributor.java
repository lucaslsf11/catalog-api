package com.portfolio.catalog.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class SystemInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();

        long heapUsedMb = memoryBean.getHeapMemoryUsage().getUsed() / (1024 * 1024);
        long heapMaxMb = memoryBean.getHeapMemoryUsage().getMax() / (1024 * 1024);
        long uptimeMinutes = (runtimeBean.getUptime() / 1000) / 60;

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("status_aplicacao", "Operacional");
        details.put("tempo_ativo", uptimeMinutes + " minuto(s)");
        details.put("consumo_memoria", heapUsedMb + " MB de " + heapMaxMb + " MB alocados");
        details.put("versao_java", System.getProperty("java.version"));
        details.put("sistema_operacional", System.getProperty("os.name"));

        builder.withDetail("visao_geral_sistema", details);
    }
}