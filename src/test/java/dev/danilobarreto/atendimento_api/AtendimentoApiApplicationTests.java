package dev.danilobarreto.atendimento_api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.ClassNameFilter;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

public class AtendimentoApiApplicationTests {

    @Test
    void deveExecutarTodosOsTestesPresentesNoPacote() {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectPackage("dev.danilobarreto.atendimento_api"))
                .filters(ClassNameFilter.excludeClassNamePatterns(".*AtendimentoApiApplicationTests"))
                .build();

        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        long failures = summary.getTotalFailureCount();
        if (failures > 0) {
            StringBuilder sb = new StringBuilder();
            summary.getFailures().forEach(f -> sb.append(f.getTestIdentifier()).append(" : ").append(f.getException()).append("\n"));
            Assertions.fail("Found " + failures + " test failures:\n" + sb.toString());
        }
    }
}
