package io.github.giannialberico.quarkus.lab.extension.runtime;

import io.quarkus.runtime.annotations.Recorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

@Recorder
public class MetadataRecorder {

    Logger logger = LoggerFactory.getLogger(MetadataRecorder.class);

    public void printBuildDateAndDeps(long instant, List<String> deps) {
        logger.info("application build date: {}", new Date(instant));
        logger.info("{} application dependencies: {}", deps.size(), deps);
    }
}
