package org.jmmo.hessian.example.server;

import io.vertx.core.cli.CLIException;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.spi.launcher.DefaultCommand;

@Name("my")
@Summary("Start Hessian service")
public class MyCommand extends DefaultCommand {

    @Override
    public void run() throws CLIException {
        System.out.println(executionContext.commandLine().allArguments());
    }
}
