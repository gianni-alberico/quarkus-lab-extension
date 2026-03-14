package io.github.giannialberico.quarkus.lab.extension.deployment;

import io.github.giannialberico.quarkus.lab.extension.runtime.ExtensionBean;
import io.github.giannialberico.quarkus.lab.extension.runtime.MetadataRecorder;
import io.github.giannialberico.quarkus.lab.extension.runtime.impl.Blue;
import io.github.giannialberico.quarkus.lab.extension.runtime.impl.Red;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.pkg.builditem.CurateOutcomeBuildItem;
import io.quarkus.maven.dependency.ResolvedDependency;
import io.smallrye.common.annotation.Identifier;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Singleton;
import org.jboss.jandex.ClassInfo;
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
                deps.stream()
                        .map(dep -> dep.getArtifactId() + ":" + dep.getVersion())
                        .toList()
        );
    }

    @BuildStep
    public void registerCloneableClasses(
            CombinedIndexBuildItem combinedIndexBuildItem,
            BuildProducer<ReflectiveClassBuildItem> reflectionClasses
    ) {

        Collection<ClassInfo> cloneableClasses = combinedIndexBuildItem.getIndex()
                .getAllKnownImplementations(Cloneable.class);

        log.info("found {} cloneable classes", cloneableClasses.size());

        for (ClassInfo c : cloneableClasses) {
            log.info("Registering class {} for reflection because it implements Cloneable", c);
            reflectionClasses.produce(
                    ReflectiveClassBuildItem.builder(c.name().toString()).methods().build()
            );
        }
    }

    @BuildStep
    public AdditionalBeanBuildItem registerColoredInterfaceImplementation(CombinedIndexBuildItem combinedIndexBuildItem) {
        Class<?> implementation = Blue.class;

        if(!combinedIndexBuildItem.getIndex().getAnnotations(RunOnVirtualThread.class).isEmpty()) {
            implementation = Red.class;
        }

        log.info("registering {} as Colored implementation", implementation.getName());
        return AdditionalBeanBuildItem.unremovableOf(implementation);
    }

    @Record(ExecutionTime.STATIC_INIT)
    @BuildStep
    public void registerSyntheticBeans(LabBuildTimeConfig labBuildTimeConfig, BuildProducer<SyntheticBeanBuildItem> syntheticBeanProducer, MetadataRecorder metadataRecorder) {
        labBuildTimeConfig.beans()
                .ifPresent(beans -> beans.forEach(bean -> {
                    log.info("registering {} as synthetic bean", bean);

                    syntheticBeanProducer.produce(
                            SyntheticBeanBuildItem
                                    .configure(ExtensionBean.class)
                                    .scope(Singleton.class)
                                    .unremovable()
                                    .addQualifier()
                                        .annotation(Identifier.class)
                                        .addValue("value", bean)
                                        .done()
                                    .supplier(metadataRecorder.extensionBeanSupplier(bean))
                                    .done()
                    );
        }));
    }
}
