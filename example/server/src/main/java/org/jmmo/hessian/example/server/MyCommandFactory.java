package org.jmmo.hessian.example.server;

import io.vertx.core.spi.launcher.DefaultCommandFactory;

public class MyCommandFactory extends DefaultCommandFactory<MyCommand> {

    public MyCommandFactory() {
        super(MyCommand.class);
    }
}
