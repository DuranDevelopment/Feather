package cc.ddev.feather.logger;

import cc.ddev.feather.Server;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
    @Getter
    public static final Logger logger = LoggerFactory.getLogger(Server.class);
}
