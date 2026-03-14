package io.github.giannialberico.quarkus.lab.extension.deployment;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

import java.util.List;
import java.util.Optional;

@ConfigMapping(prefix = "quarkus.lab-extension")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface LabBuildTimeConfig {

    /**
     * List of named beans to create synthetically at build time.
     * <p>
     * Each entry in the list will result in a synthetic CDI bean named with {@code @Identifier},
     * injectable in the application using {@code @Identifier("name")}.
     * </p>
     * Example:
     * <pre>
     *     quarkus.lab-extension.beans=name,name2
     * </pre>
     */
    Optional<List<String>> beans();
}