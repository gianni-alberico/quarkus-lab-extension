package io.github.giannialberico.quarkus.lab.extension.deployment;

import io.github.giannialberico.quarkus.lab.extension.runtime.MetadataRecorder;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.pkg.builditem.CurateOutcomeBuildItem;
import io.quarkus.maven.dependency.ResolvedDependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

class QuarkusLabExtensionProcessor {

    private static final String FEATURE = "quarkus-lab-extension";
    private static final Logger log = LoggerFactory.getLogger(QuarkusLabExtensionProcessor.class);

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @Record(ExecutionTime.RUNTIME_INIT)
    @BuildStep
    public void registerBuildDateAndDeps(CurateOutcomeBuildItem curateOutcomeBuildItem, MetadataRecorder metadataRecorder) {
        Collection<ResolvedDependency> deps = curateOutcomeBuildItem.getApplicationModel().getDependencies().
                stream()
                .filter(ResolvedDependency::isRuntimeCp)
                .toList();

        log.info("found {} application dependencies", deps.size());
        metadataRecorder.printBuildDateAndDeps(
                System.currentTimeMillis(),
                deps.stream().map(ResolvedDependency::getArtifactId).toList()
        );
    }
}
